package com.example.chiragdarji.androidmvvmsample.data

import com.example.chiragdarji.androidmvvmsample.Data
import com.example.chiragdarji.androidmvvmsample.Post
import com.example.chiragdarji.androidmvvmsample.SunsetRise.SunSetRiseResponse
import com.example.chiragdarji.androidmvvmsample.WeatherPojo.WeatherResponse
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit






class RetrofitSingleton {

    private var client: GetApiClient


    constructor(){
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
        val gson = GsonBuilder()
            .setLenient()
            .create()


        val retrofit = Retrofit.Builder().baseUrl(URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient).build()


        client = retrofit.create(GetApiClient::class.java)
    }

    companion object {
        @JvmField
        public val URL = "http://api.openweathermap.org/data/2.5/"
        //public val URL = "http://192.168.3.29/"
        @JvmField
        public val extraParam = "weather?q=Ahmedabad&APPID=ea574594b9d36ab688642d5fbeab847e"

        @Volatile
        private var instance: RetrofitSingleton? = null
        @JvmStatic
        fun getInstance(): RetrofitSingleton? {
            if (instance == null) {
                synchronized(RetrofitSingleton::class.java) {
                    if (instance == null) {
                        instance = RetrofitSingleton()
                    }
                }
            }
            return instance
        }
    }

    fun provideClient(): GetApiClient {
        return client
    }

   public interface GetApiClient {
       @GET
       abstract fun getProject(@Url url: String): Call<WeatherResponse>

       //https://api.sunrise-sunset.org/json?lat=36.7201600&lng=-4.4203400&date=today
       @GET
       abstract fun getSunsetRise(@Url url: String): Call<SunSetRiseResponse>

       @Headers( "Accept: application/json")
       @POST("devicetracking/api.php")
       abstract fun getCheckout(@Body post: Post): Call<Data>
   }
}