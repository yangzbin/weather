package com.sitop.coolweather.gson

/**
 * Created by Yangzb on 2018/8/28 15:20
 * E-mail：yangzongbin@si-top.com
 * Decsribe:
 */
class AQI {
    var city:AQICity?=null

    class AQICity {
        var aqi: String? = null
        var pm25: String? = null
    }
}