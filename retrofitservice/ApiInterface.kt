package com.cell_tower.webservices.interfaces.api

import com.cell_tower.webservices.requestpojos.ChangePasswordRequest
import com.cell_tower.webservices.requestpojos.LoginRequest
import com.cell_tower.webservices.requestpojos.StatusRequestBean
import com.cell_tower.webservices.responsepojos.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    /**
     * User LoggedIn
     */
    @Headers("Content-Type: application/json")
    @POST( "auth/signin")
    fun getUserLogin(@Body loginRequest: LoginRequest): Call<ResponseBody>

    /*update User Profile*/
    @Headers("Content-Type: application/json")
    @PUT( "users/{id}")
    fun getUpdateUser(@Path("id") id : String, @Body loginRequest: LoginResponse.UserInfo, @Header("authorization") auth : String): Call<ResponseBody>

    /*Dashboard Count*/
    @Headers("Content-Type: application/json")
    @GET( "dashboard/statistics")
    fun getDashboardCount(@Header("authorization") auth : String): Call<ResponseBody>

    /*Dashboard : Location List*/
    @Headers("Content-Type: application/json")
    @GET( "locations/list")
    fun getLocationList(@Header("authorization") auth : String): Call<ResponseBody>

    /*Location Detail*/
    @Headers("Content-Type: application/json")
    @GET( "locations/{id}")
    fun getLocationDetail(@Path("id") id : String,@Header("authorization") auth : String): Call<ResponseBody>

    /*Alert List*//*
    @Headers("Content-Type: application/json")
    @GET( "alerts/list")
    fun getAlertList(@Header("authorization") auth : String): Call<ResponseBody>*/

    /*Alert List*/
    @Headers("Content-Type: application/json")
    @GET( "locations/towerdevice/{locationId}")
    fun getAlertList(@Path("locationId") locationId : String,@Header("authorization") auth : String): Call<ResponseBody>

    /*Assets List*/
    @Headers("Content-Type: application/json")
    @GET( "devices/list/{id}")
    fun getAssetList(@Path("id") id : String,@Header("authorization") auth : String): Call<ResponseBody>

    /*Tower Detail*/
    @Headers("Content-Type: application/json")
    @GET( "devices/{id}")
    fun getTowerDetail(@Path("id") id : String,@Header("authorization") auth : String): Call<ResponseBody>

    /*Change Password*/
    @Headers("Content-Type: application/json")
    @POST( "auth/changepassword")
    fun getChangePwd(@Header("authorization") auth : String,@Body changePWDRequest: ChangePasswordRequest): Call<ResponseBody>

    /*Change Password*/
    @Headers("Content-Type: application/json")
    @GET/*( "auth/changepassword")*/
    fun getWeatherData(@Url url : String): Call<ResponseBody>

    /*Get Sensor List*/
    @Headers("Content-Type: application/json")
    @GET( "devices/sensor/{deviceId}")
    fun getSensorList(@Path("deviceId") id : String, @Header("authorization") auth : String): Call<ResponseBody>

    /*change device status*/
    @Headers("Content-Type: application/json")
    @PUT( "devices/updatestatus/{deviceId}")
    fun getDeviceStatus(@Body status : StatusRequestBean,@Path("deviceId") id : String, @Header("authorization") auth : String): Call<ResponseBody>

    /*Get Device Alert List*/
    @Headers("Content-Type: application/json")
    @GET( "alerts/devicealertlist")
    fun getDeviceAlertList(@Header("authorization") auth : String): Call<ResponseBody>
}