package com.sitop.coolweather.gson

import com.google.gson.annotations.SerializedName

/**
 * Created by Yangzb on 2018/8/28 15:31
 * E-mail：yangzongbin@si-top.com
 * Decsribe:
 */
class Forecast {
    var date: String? = null
    @SerializedName("tmp")
    var temperature: Temperature? = null
    @SerializedName("cond")
    var more: More? = null

    class Temperature {
        var max:String?=null
        var min:String?=null
    }

    class More {
        @SerializedName("txt_d")
        var info:String?=null
    }

}