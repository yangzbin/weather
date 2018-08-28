package com.sitop.coolweather.util

import com.sitop.coolweather.db.City
import com.sitop.coolweather.db.County
import com.sitop.coolweather.db.Province
import org.json.JSONArray
import org.json.JSONException


/**
 * Created by Yangzb on 2018/8/28 10:33
 * E-mail：yangzongbin@si-top.com
 * Decsribe:
 */
object Utility {
    /**
     * 解析省级数据
     */
    fun handleProvinceResponse(response: String): Boolean {
        if (!response.isNullOrEmpty()) {
            try {
                val allProvinces = JSONArray(response)
                var i = 0
                while (i < allProvinces.length()) {
                    val proviceObject = allProvinces.getJSONObject(i)
                    val proviceCode = proviceObject.getInt("id")
                    val provinceName = proviceObject.getString("name")
                    val province = Province(proviceCode,provinceName,proviceCode)
                    province.save()
                    i++
                }
                return true
            }catch (e:JSONException){
                e.printStackTrace()
            }
        }
        return false
    }

    /**
     *解析市级数据
     */
    fun handleCityResponse(response:String,provinceId:Int):Boolean{
        if(!response.isNullOrEmpty()){
            try {
                val allCities = JSONArray(response)
                var i=0
                while (i<allCities.length()){
                    val cityObject = allCities.getJSONObject(i)
                    val cityCode = cityObject.getInt("id")
                    val cityName = cityObject.getString("name")
                    val city = City(cityCode,cityName,cityCode,provinceId)
                    city.save()
                    i++
                }
                return true
            }catch (e:JSONException){
                e.printStackTrace()
            }
        }
        return false
    }
    /**
     * 解析县级数据
     */
    fun handleCountyResponse(response: String,cityId:Int):Boolean{
        if(!response.isNullOrEmpty()){
            try {
                val allCounties = JSONArray(response)
                var i=0
                while (i<allCounties.length()){
                    val countyObject = allCounties.getJSONObject(i)
                    val countyCode = countyObject.getInt("id")
                    val countyName = countyObject.getString("name")
                    val wheatherId = countyObject.getString("weather_id")
                    val county = County(countyCode,countyName,wheatherId,cityId)
                    county.save()
                    i++
                }
                return true
            }catch (e:JSONException){
                e.printStackTrace()
            }
        }
        return false
    }
}