package com.sitop.coolweather.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import android.preference.PreferenceManager
import com.sitop.coolweather.WeatherActivity
import com.sitop.coolweather.gson.Weather
import com.sitop.coolweather.util.HttpUtil
import com.sitop.coolweather.util.Utility
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class AutoUpdateService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        updateWeather()
        updateBingPic()
        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val anHour = 8*60*60*1000 //8小时毫秒数
        val triggerAtTime = SystemClock.elapsedRealtime()+anHour //SystemClock.elapsedRealtime() 系统开机经过多长时间
        val i = Intent(this,AutoUpdateService::class.java)
        val pi = PendingIntent.getService(this,0,i,0)
        manager.cancel(pi)
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun updateBingPic() {
        val requestPic = "http://guolin.tech/api/bing_pic"
        HttpUtil.sendOkHttpRequest(requestPic,object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                var bingPic = response.body()?.string()
                val editor = PreferenceManager.getDefaultSharedPreferences(this@AutoUpdateService)
                        .edit()
                editor.putString("bing_pic",bingPic)
                editor.apply()
            }

        })
    }

    private fun updateWeather() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val weatherString = prefs.getString("weather",null)
        if(!weatherString.isNullOrEmpty()){
            val weather = Utility.handleWeatherResponse(weatherString) as Weather
            val weatherId = weather.basic?.weatherId
            val weatherUrl = "http://guolin.tech/api/weather?cityid=$weatherId&key=${WeatherActivity.KEY}"
            HttpUtil.sendOkHttpRequest(weatherUrl,object :Callback{
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseText = response.body()?.string()
                    val weather = Utility.handleWeatherResponse(responseText!!) as Weather
                    if(weather != null && weather.status.equals("ok")){
                        val editor = PreferenceManager.getDefaultSharedPreferences(this@AutoUpdateService)
                                .edit()
                        editor.putString("weather",responseText)
                        editor.apply()
                    }
                }

            })
        }
    }
}
