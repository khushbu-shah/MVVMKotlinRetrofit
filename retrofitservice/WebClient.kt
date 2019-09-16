package com.cell_tower.webservices.servicemodel

import com.cell_tower.common.WsConst
import com.cell_tower.webservices.interfaces.api.ApiInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

internal object WebClient
{
    private lateinit var apiInterface: ApiInterface
    private lateinit var apiInterfaceWeather: ApiInterface

    init {
        setupClient()
        setupWeatherClient()
    }

    private fun setupClient()
    {
        val okHttpLogging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
        okHttpLogging.level = HttpLoggingInterceptor.Level.BODY

        val okHttpBuilder = OkHttpClient.Builder()
        okHttpBuilder.addInterceptor(okHttpLogging)

        okHttpBuilder.connectTimeout(5, TimeUnit.MINUTES)
        okHttpBuilder.readTimeout(5, TimeUnit.MINUTES)
        okHttpBuilder.retryOnConnectionFailure(true)
        // okHttpBuilder.addInterceptor(CustomResponseInterceptor())

        //val okHttpLogging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)

        okHttpBuilder.addInterceptor(okHttpLogging)

        val apiClient = Retrofit.Builder()
            .baseUrl(WsConst.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpBuilder.build())
            .build()
        apiInterface = apiClient.create(ApiInterface::class.java)
    }

    private fun setupWeatherClient()
    {
        val okHttpLogging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
        okHttpLogging.level = HttpLoggingInterceptor.Level.BODY

        val okHttpBuilder = OkHttpClient.Builder()
        okHttpBuilder.addInterceptor(okHttpLogging)

        okHttpBuilder.connectTimeout(5, TimeUnit.MINUTES)
        okHttpBuilder.readTimeout(5, TimeUnit.MINUTES)
        okHttpBuilder.retryOnConnectionFailure(true)
        // okHttpBuilder.addInterceptor(CustomResponseInterceptor())

        //val okHttpLogging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)

        okHttpBuilder.addInterceptor(okHttpLogging)

        val apiClient = Retrofit.Builder()
            .baseUrl(WsConst.WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpBuilder.build())
            .build()
        apiInterfaceWeather = apiClient.create(ApiInterface::class.java)
    }

    fun getApiClient(): ApiInterface = apiInterface
    fun getWeatherApiClient(): ApiInterface = apiInterfaceWeather
}