package com.sitop.coolweather.gson

import com.google.gson.annotations.SerializedName

/**
 * Created by Yangzb on 2018/8/28 15:23
 * E-mail：yangzongbin@si-top.com
 * Decsribe:
 */
class Now {
    @SerializedName("tmp")
    var temperature:String?=null
    @SerializedName("cond")
    var more:More?=null

    class More {
        @SerializedName("txt")
        var info:String?=null
    }

}