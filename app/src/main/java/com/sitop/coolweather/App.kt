package com.sitop.coolweather

import android.app.Application
import org.litepal.LitePal

/**
 * Created by Yangzb on 2018/8/28 14:21
 * E-mail：yangzongbin@si-top.com
 * Decsribe:
 */
class App: Application() {
    companion object {
        private var instance:Application?=null
        fun instance() = instance!!
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        LitePal.initialize(this)
    }
}