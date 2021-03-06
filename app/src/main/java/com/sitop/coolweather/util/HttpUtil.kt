package com.sitop.coolweather.util

import android.util.Log
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Created by Yangzb on 2018/8/28 10:22
 * E-mail：yangzongbin@si-top.com
 * Describe:网络请求工具类
 */
object HttpUtil {
    fun sendOkHttpRequest(address:String,callback:Callback){
        Log.e("request",address)
        val client = OkHttpClient()
        val request = Request.Builder().url(address).build()
        client.newCall(request).enqueue(callback)
    }
}