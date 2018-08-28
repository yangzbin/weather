package com.sitop.coolweather

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        if(!prefs.getString("weather",null).isNullOrEmpty()){
            startActivity(Intent(this,WeatherActivity::class.java))
            finish()
        }
    }
}
