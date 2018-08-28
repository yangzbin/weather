package com.sitop.coolweather

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.sitop.coolweather.gson.Weather
import com.sitop.coolweather.util.HttpUtil
import com.sitop.coolweather.util.Utility
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.aqi.*
import kotlinx.android.synthetic.main.forecast.*
import kotlinx.android.synthetic.main.now.*
import kotlinx.android.synthetic.main.suggestion.*
import kotlinx.android.synthetic.main.title.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class WeatherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(Build.VERSION.SDK_INT >= 21){
            val decorView = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.activity_weather)
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val weatherString = prefs.getString("weather",null)
        if (!weatherString.isNullOrEmpty()){
            val weather = Utility.handleWeatherResponse(weatherString)
            showWeatherInfo(weather)
        }else{
            val weatherId = intent.getStringExtra(ChooseAreaFragment.WEATHER_ID)
            id_scroll_weather.visibility = View.INVISIBLE
            requestWeather(weatherId)
        }
        val picUrl = prefs.getString("bing_pic", null)
        if(!picUrl.isNullOrEmpty()){
            Glide.with(this).load(picUrl).into(id_img_pic)
        }else{
            loadBingPic()
        }
    }

    private fun loadBingPic() {
        val requestPic = "http://guolin.tech/api/bing_pic"
        HttpUtil.sendOkHttpRequest(requestPic,object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val bingPic = response.body()?.string()
                val editor = PreferenceManager.getDefaultSharedPreferences(this@WeatherActivity).edit()
                editor.putString("bing_pic",bingPic)
                editor.apply()
                runOnUiThread {
                    Glide.with(this@WeatherActivity).load(bingPic).into(id_img_pic)
                }
            }

        })
    }

    private fun requestWeather(weatherId: String?) {
        val key = "bc0418b57b2d4918819d3974ac1285d9"
        val weatherUrl = "http://guolin.tech/api/weather?cityid=$weatherId&key=$key"
        HttpUtil.sendOkHttpRequest(weatherUrl,object :Callback{
            override fun onResponse(call: Call, response: Response) {
                val responseText = response.body()?.string()
                val weather = Utility.handleWeatherResponse(responseText!!)
                runOnUiThread {
                    if (weather!=null && weather.status.equals("ok")){
                        val editor = PreferenceManager.getDefaultSharedPreferences(this@WeatherActivity).edit()
                        editor.putString("weather",responseText)
                        editor.apply()
                        showWeatherInfo(weather)
                    }else{
                        Toast.makeText(this@WeatherActivity,"获取天气信息失败",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(this@WeatherActivity,"获取天气信息失败",Toast.LENGTH_SHORT).show()
            }

        })
        loadBingPic()
    }

    private fun showWeatherInfo(weather: Weather?) {
        val cityName = weather?.basic?.cityName
        val updateTime = weather?.basic?.update?.updateTime?.split(" ")?.get(1)
        val degree = weather?.now?.temperature+"℃"
        val weatherInfo = weather?.now?.more?.info
        id_tv_city.text = cityName
        id_tv_update_time.text = updateTime
        id_tv_degree.text = degree
        id_tv_weather_info.text = weatherInfo

        id_ll_forecast.removeAllViews()
        for (item in weather?.forecastList!!){
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item,id_ll_forecast,false)
            view.findViewById<TextView>(R.id.id_tv_date).text = item.date
            view.findViewById<TextView>(R.id.id_tv_info).text = item.more?.info
            view.findViewById<TextView>(R.id.id_tv_max).text = item.temperature?.max
            view.findViewById<TextView>(R.id.id_tv_min).text = item.temperature?.min
            id_ll_forecast.addView(view)
        }
        if (weather?.aqi!=null){
            id_tv_aqi.text = weather.aqi?.city?.aqi
            id_tv_pm25.text = weather.aqi?.city?.pm25
        }
        id_tv_comfort.text = "舒适度：${weather?.suggestion?.comform?.info}"
        id_tv_car_wash.text = "汽车指数：${weather?.suggestion?.carWash?.info}"
        id_tv_sport.text = "运动建议：${weather?.suggestion?.sport?.info}"
        id_scroll_weather.visibility = View.VISIBLE
    }
}
