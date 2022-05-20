package com.sunnyweather.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SunnyWeatherApplication :Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        const val TOKEN = "I6yDP06HvyvT1kId"
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}