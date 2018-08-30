package com.sitop.coolweather

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.sitop.coolweather.db.City
import com.sitop.coolweather.db.County
import com.sitop.coolweather.db.Province
import com.sitop.coolweather.util.HttpUtil
import com.sitop.coolweather.util.Utility
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.choose_area.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.litepal.LitePal
import java.io.IOException

/**
 * Created by Yangzb on 2018/8/28 11:34
 * E-mail：yangzongbin@si-top.com
 * Describe:
 */
class ChooseAreaFragment : Fragment() {
    companion object {
        const val LEVEL_PROVINCE = 0
        const val LEVEL_CITY = 1
        const val LEVEL_COUNTY = 2

        const val PROVINCE = "province"
        const val CITY = "city"
        const val COUNTY = "county"

        const val WEATHER_ID = "weather_id"
    }

    private var progressDialog: ProgressDialog?=null
    lateinit var adapter:ArrayAdapter<String>
    private var dataList = ArrayList<String>()
    lateinit var provinceList: List<Province>
    lateinit var cityList:List<City>
    lateinit var countyList:List<County>
    private var selectedProvince:Province? = null
    private var selectedCity:City?=null
    private var currentLevel = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.choose_area,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ArrayAdapter(activity,android.R.layout.simple_list_item_1,dataList)
        id_list_view.adapter = adapter

        id_list_view.setOnItemClickListener { _, _, position, _ ->
            when(currentLevel){
                LEVEL_PROVINCE ->{
                    selectedProvince = provinceList[position];
                    queryCities()
                }
                LEVEL_CITY ->{
                    selectedCity = cityList[position]
                    queryCounties()
                }
                LEVEL_COUNTY ->{
                    val weatherId = countyList[position].weatherId
                    if (activity is MainActivity){
                        var intent = Intent(activity,WeatherActivity::class.java)
                        intent.putExtra(WEATHER_ID,weatherId)
                        startActivity(intent)
                        activity?.finish()
                    }else if (activity is WeatherActivity){
                        val ac = activity as WeatherActivity
                        ac.id_drawer_layout.closeDrawers()
                        ac.id_swipe_refresh.isRefreshing = true
                        ac.requestWeather(weatherId)
                    }
                }
            }
        }

        id_btn_back.setOnClickListener {
            when(currentLevel){
                LEVEL_COUNTY ->{
                    queryCities()
                }
                LEVEL_CITY ->{
                    queryProvinces()
                }
            }
        }
        queryProvinces()
    }
    private fun queryProvinces() {
        id_tv_title.text = "中国"
        id_btn_back.visibility = View.GONE
        provinceList = LitePal.findAll(Province::class.java)
        if(provinceList.isNotEmpty()){
            dataList.clear()
            for (item in provinceList){
                dataList.add(item.provinceName)
            }
            adapter.notifyDataSetChanged()
            id_list_view.setSelection(0)
            currentLevel = LEVEL_PROVINCE
        }else{
            val address = "http://guolin.tech/api/china"
            queryFromServer(address, PROVINCE)
        }
    }

    private fun queryCities() {
        id_tv_title.text = selectedProvince?.provinceName
        id_btn_back.visibility = View.VISIBLE
        cityList = LitePal.where("provinceid = ?", selectedProvince?.provinceCode.toString()).find(City::class.java)
        if(cityList.isNotEmpty()){
            dataList.clear()
            for (item in cityList){
                dataList.add(item.cityName)
            }
            adapter.notifyDataSetChanged()
            id_list_view.setSelection(0)
            currentLevel = LEVEL_CITY
        }else{
            val provinceCode = selectedProvince?.provinceCode
            val address = "http://guolin.tech/api/china/$provinceCode"
            queryFromServer(address, CITY)
        }
    }

    private fun queryCounties() {
        id_tv_title.text = selectedCity?.cityName
        id_btn_back.visibility = View.VISIBLE
        countyList = LitePal.where("cityid = ?",selectedCity?.cityCode.toString()).find(County::class.java)
        if (countyList.isNotEmpty()){
            dataList.clear()
            for (item in countyList){
                dataList.add(item.countyName)
            }
            adapter.notifyDataSetChanged()
            id_list_view.setSelection(0)
            currentLevel = LEVEL_COUNTY
        }else{
            val provinceCode = selectedProvince?.provinceCode
            val cityCode = selectedCity?.cityCode
            val address = "http://guolin.tech/api/china/$provinceCode/$cityCode"
            queryFromServer(address, COUNTY)
        }
    }
    private fun queryFromServer(address: String, type: String) {
        showProgressDialog()
        HttpUtil.sendOkHttpRequest(address,object :Callback{
            override fun onResponse(call: Call, response: Response) {
                val responseText = response.body()?.string()
                var result = false
                when(type){
                    PROVINCE ->{
                        result = Utility.handleProvinceResponse(responseText!!)
                    }
                    CITY ->{
                        result = Utility.handleCityResponse(responseText!!, selectedProvince!!.provinceCode)
                    }
                    COUNTY ->{
                        result = Utility.handleCountyResponse(responseText!!,selectedCity!!.cityCode)
                    }
                }
                if (result){
                    activity?.runOnUiThread {
                        closeProgressDialog()
                        when(type){
                            PROVINCE->{
                                queryProvinces()
                            }
                            CITY->{
                                queryCities()
                            }
                            COUNTY->{
                                queryCounties()
                            }
                        }
                    }
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread{
                    closeProgressDialog()
                    Toast.makeText(activity,"加载失败",Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun closeProgressDialog() {
        if(progressDialog != null){
            progressDialog?.dismiss()
        }
    }

    private fun showProgressDialog() {
        if(progressDialog == null){
            progressDialog = ProgressDialog(activity)
            progressDialog?.setMessage("正在加载...")
            progressDialog?.setCanceledOnTouchOutside(false)
        }
        progressDialog?.show()
    }
}