package com.sitop.coolweather.db

import org.litepal.crud.LitePalSupport

/**
 * Created by Yangzb on 2018/8/28 10:12
 * E-mail：yangzongbin@si-top.com
 * Decsribe:省
 */
data class Province(var id:Int,var provinceName: String,var provinceCode:Int): LitePalSupport()
