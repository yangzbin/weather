package com.sitop.coolweather.gson

import com.google.gson.annotations.SerializedName

/**
 * Created by Yangzb on 2018/8/28 15:16
 * E-mail：yangzongbin@si-top.com
 * Decsribe:
 */
class Basic {
    @SerializedName("city")
    var cityName:String?=null
    @SerializedName("id")
    var weatherId:String?=null
    var update:Update?=null

    class Update {
        @SerializedName("loc")
        var updateTime:String?=null
    }
}