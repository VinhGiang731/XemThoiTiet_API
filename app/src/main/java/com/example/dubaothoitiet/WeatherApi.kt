package com.example.dubaothoitiet

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKet: String,
        @Query("units") units: String = "metric"
    ): Response<WeatherResponse>
}