package com.sitop.coolweather.gson

import com.google.gson.annotations.SerializedName

/**
 * Created by Yangzb on 2018/8/28 15:34
 * E-mail：yangzongbin@si-top.com
 * Decsribe:
 */
class Weather {
    var status: String? = null
    var basic: Basic? = null
    var aqi: AQI? = null
    var now: Now? = null
    var suggestion: Suggestion? = null
    @SerializedName("daily_forecast")
    var forecastList: List<Forecast>? = null
}