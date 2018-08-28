package com.sitop.coolweather.gson

import com.google.gson.annotations.SerializedName

/**
 * Created by Yangzb on 2018/8/28 15:25
 * E-mail：yangzongbin@si-top.com
 * Decsribe:
 */
class Suggestion {
    @SerializedName("comf")
    var comform:Comfort?=null
    @SerializedName("cw")
    var carWash:CarWash?=null
    var sport:Sport?=null

    class Sport {
        @SerializedName("txt")
        var info:String?=null
    }

    class CarWash {
        @SerializedName("txt")
        var info:String?=null
    }

    class Comfort {
        @SerializedName("txt")
        var info:String?=null
    }
}