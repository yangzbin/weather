package com.sitop.coolweather.db

import org.litepal.crud.LitePalSupport

/**
 * Created by Yangzb on 2018/8/28 10:16
 * E-mail：yangzongbin@si-top.com
 * Decsribe:市
 */
data class City(var id:Int,var cityName:String,var cityCode:Int,var provinceId:Int): LitePalSupport()