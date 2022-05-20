package com.sunnyweather.sunnyweather.logic

import Place
import androidx.lifecycle.liveData
import com.sunnyweather.sunnyweather.logic.dao.PlaceDao
import com.sunnyweather.sunnyweather.logic.model.Weather
import com.sunnyweather.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

object Repository {
    //注意数据操作子线程运行

    fun searchPlaces(query: String) =  fire(Dispatchers.IO) {
        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }


    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async {
                SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
            }
            val deferredDaily = async {
                SunnyWeatherNetwork.getDailyWeather(lng, lat)
            }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather = Weather(realtimeResponse.result.realtime,
                    dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.status}" +
                                "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }
    /*在fire()函数的内部会先调用一下liveData()函数，然后在
    liveData()函数的代码块中统一进行了try catch处理，并在try语句中调用传入的Lambda
    表达式中的代码，最终获取Lambda表达式的执行结果并调用emit()方法发射出去。
    另外还有一点需要注意，在liveData()函数的代码块中，我们是拥有挂起函数上下文的，可
    是当回调到Lambda表达式中，代码就没有挂起函数上下文了，但实际上Lambda表达式中的
    代码一定也是在挂起函数中运行的。为了解决这个问题，我们需要在函数类型前声明一个
    suspend关键字，以表示所有传入的Lambda表达式中的代码也是拥有挂起函数上下文的。*/
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }
    //实现记忆搜索地址(数据操作不宜在主线进行)
    fun savePlace(place: Place) = PlaceDao.savePlace(place)
    fun getSavedPlace() = PlaceDao.getSavedPlace()
    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
}
