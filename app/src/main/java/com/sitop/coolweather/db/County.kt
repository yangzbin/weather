package com.sitop.coolweather.db

import org.litepal.crud.LitePalSupport

/**
 * Created by Yangzb on 2018/8/28 10:18
 * E-mail：yangzongbin@si-top.com
 * Decsribe:县
 */
data class County(var id:Int,var countyName:String,var weatherId:String,var cityId:Int):LitePalSupport()