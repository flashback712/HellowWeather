package com.sunnyweather.sunnyweather.ui.weather

import Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.sunnyweather.logic.Repository

class WeatherViewModel : ViewModel() {
    private val locationLiveData = MutableLiveData<Location>()
    //这3个变量，它们都是和界面相关的数据，放到ViewModel中可以保证它们在手机屏幕发生旋
    //转的时候不会丢失
    var locationLng = ""
    var locationLat = ""
    var placeName = ""
    //为保证数据封装行设计的观察对象供activity
    val weatherLiveData = Transformations.switchMap(locationLiveData) { location ->
        Repository.refreshWeather(location.lng, location.lat)
    }

    fun refreshWeather(lng: String, lat: String) {
        locationLiveData.value = Location(lng, lat)
    }
}