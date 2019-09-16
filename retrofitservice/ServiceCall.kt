package com.cell_tower.webservices.servicemodel

import android.content.Context
import com.cell_tower.R
import com.cell_tower.common.WsConst
import com.cell_tower.utils.AppPreferences
import com.cell_tower.webservices.enums.HttpResponseCodes
import com.cell_tower.webservices.interfaces.response.ResponseInterface
import com.cell_tower.webservices.requestpojos.ChangePasswordRequest
import com.cell_tower.webservices.requestpojos.LoginRequest
import com.cell_tower.webservices.requestpojos.StatusRequestBean
import com.cell_tower.webservices.responsepojos.*
import com.cell_tower.webservices.responsepojos.WeatherPojo.WeatherResponse
import com.cell_tower.webservices.servicemodel.CommonServiceUtils.checkIsSuccessResponse
import com.cell_tower.webservices.servicemodel.CommonServiceUtils.getResponseFromBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

internal object ServiceCall
{
    fun callLogin(context: Context, loginRequest: LoginRequest, responseInterface: ResponseInterface)
    {
        val call = WebClient.getApiClient().getUserLogin(loginRequest)
        call.enqueue(object : retrofit2.Callback<ResponseBody>
        {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?)
            {
                val baseResponse = checkIsSuccessResponse(response, context)
                if (baseResponse != null)
                {
                    if (baseResponse.isSuccessful)
                    {

                        responseInterface.onResponseSuccess(loginRequest,
                            getResponseFromBody(response!!.body()!!, clazz = LoginResponse::class.java))
                    }
                    else
                    {
                        responseInterface.onResponseError(loginRequest, baseResponse.httpCode, baseResponse.code, baseResponse.msg)
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<ResponseBody>?, t: Throwable?)
            {
                responseInterface.onResponseError(loginRequest, HttpResponseCodes.Default,
                    respMessage = context.getString(R.string.alert_sorry_something_went_wrong))
            }
        })
    }

    fun callUpdateUser(context: Context, updateProfileRequest: LoginResponse.UserInfo, strAuthToken : String, responseInterface: ResponseInterface)
    {
        val call = WebClient.getApiClient().getUpdateUser(updateProfileRequest.id!!,updateProfileRequest,strAuthToken)
        call.enqueue(object : retrofit2.Callback<ResponseBody>
        {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?)
            {
                val baseResponse = checkIsSuccessResponse(response, context)
                if (baseResponse != null)
                {
                    if (baseResponse.isSuccessful)
                    {

                        responseInterface.onResponseSuccess(updateProfileRequest,
                            getResponseFromBody(response!!.body()!!, clazz = UpdateProfileResponse::class.java))
                    }
                    else
                    {
                        responseInterface.onResponseError(updateProfileRequest, baseResponse.httpCode, baseResponse.code, baseResponse.msg)
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<ResponseBody>?, t: Throwable?)
            {
                responseInterface.onResponseError(updateProfileRequest, HttpResponseCodes.Default,
                    respMessage = context.getString(R.string.alert_sorry_something_went_wrong))
            }
        })
    }

    fun callChangePwd(context: Context, changePasswordRequest: ChangePasswordRequest, strAuthToken : String, responseInterface: ResponseInterface)
    {
        val call = WebClient.getApiClient().getChangePwd(strAuthToken,changePasswordRequest)
        call.enqueue(object : retrofit2.Callback<ResponseBody>
        {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?)
            {
                val baseResponse = checkIsSuccessResponse(response, context)
                if (baseResponse != null)
                {
                    if (baseResponse.isSuccessful)
                    {

                        responseInterface.onResponseSuccess(changePasswordRequest,
                            getResponseFromBody(response!!.body()!!, clazz = ChangePwdResponse::class.java))
                    }
                    else
                    {
                        responseInterface.onResponseError(changePasswordRequest, baseResponse.httpCode, baseResponse.code, baseResponse.msg)
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<ResponseBody>?, t: Throwable?)
            {
                responseInterface.onResponseError(changePasswordRequest, HttpResponseCodes.Default,
                    respMessage = context.getString(R.string.alert_sorry_something_went_wrong))
            }
        })
    }


    fun callDashboardCount(context: Context, strAuthToken : String, responseInterface: ResponseInterface)
    {
        val call = WebClient.getApiClient().getDashboardCount(strAuthToken)
        call.enqueue(object : retrofit2.Callback<ResponseBody>
        {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?)
            {
                val baseResponse = checkIsSuccessResponse(response, context)
                if (baseResponse != null)
                {
                    if (baseResponse.isSuccessful)
                    {

                        responseInterface.onResponseSuccess("",
                            getResponseFromBody(response!!.body()!!, clazz = DashboardCountResponse::class.java))
                    }
                    else
                    {
                        responseInterface.onResponseError("", baseResponse.httpCode, baseResponse.code, baseResponse.msg)
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<ResponseBody>?, t: Throwable?)
            {
                responseInterface.onResponseError("", HttpResponseCodes.Default,
                    respMessage = context.getString(R.string.alert_sorry_something_went_wrong))
            }
        })
    }

    fun callLocationList(context: Context, strAuthToken : String, responseInterface: ResponseInterface)
    {
        val call = WebClient.getApiClient().getLocationList(strAuthToken)
        call.enqueue(object : retrofit2.Callback<ResponseBody>
        {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?)
            {
                val baseResponse = checkIsSuccessResponse(response, context)
                if (baseResponse != null)
                {
                    if (baseResponse.isSuccessful)
                    {

                        responseInterface.onResponseSuccess("",
                            getResponseFromBody(response!!.body()!!, clazz = LocationListResponse::class.java))
                    }
                    else
                    {
                        responseInterface.onResponseError("", baseResponse.httpCode, baseResponse.code, baseResponse.msg)
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<ResponseBody>?, t: Throwable?)
            {
                responseInterface.onResponseError("", HttpResponseCodes.Default,
                    respMessage = context.getString(R.string.alert_sorry_something_went_wrong))
            }
        })
    }

    fun callLocationDetail(context: Context, strAuthToken : String,id : String, responseInterface: ResponseInterface)
    {
        val call = WebClient.getApiClient().getLocationDetail(id,strAuthToken)
        call.enqueue(object : retrofit2.Callback<ResponseBody>
        {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?)
            {
                val baseResponse = checkIsSuccessResponse(response, context)
                if (baseResponse != null)
                {
                    if (baseResponse.isSuccessful)
                    {

                        responseInterface.onResponseSuccess("",
                            getResponseFromBody(response!!.body()!!, clazz = LocationDetailResponse::class.java))
                    }
                    else
                    {
                        responseInterface.onResponseError("", baseResponse.httpCode, baseResponse.code, baseResponse.msg)
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<ResponseBody>?, t: Throwable?)
            {
                responseInterface.onResponseError("", HttpResponseCodes.Default,
                    respMessage = context.getString(R.string.alert_sorry_something_went_wrong))
            }
        })
    }

    fun callAlertList(context: Context,locationId : String, strAuthToken : String, responseInterface: ResponseInterface)
    {
        val call = WebClient.getApiClient().getAlertList(locationId,strAuthToken)
        call.enqueue(object : retrofit2.Callback<ResponseBody>
        {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?)
            {
                val baseResponse = checkIsSuccessResponse(response, context)
                if (baseResponse != null)
                {
                    if (baseResponse.isSuccessful)
                    {
                        responseInterface.onResponseSuccess("",
                            getResponseFromBody(response!!.body()!!, clazz = AlertListResponse::class.java))
                    }
                    else
                    {
                        responseInterface.onResponseError("", baseResponse.httpCode, baseResponse.code, baseResponse.msg)
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<ResponseBody>?, t: Throwable?)
            {
                responseInterface.onResponseError("", HttpResponseCodes.Default,
                    respMessage = context.getString(R.string.alert_sorry_something_went_wrong))
            }
        })
    }

    fun callAssetList(context: Context, strAuthToken : String,id : String, responseInterface: ResponseInterface)
    {
        val call = WebClient.getApiClient().getAssetList(id,strAuthToken)
        call.enqueue(object : retrofit2.Callback<ResponseBody>
        {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?)
            {
                val baseResponse = checkIsSuccessResponse(response, context)
                if (baseResponse != null)
                {
                    if (baseResponse.isSuccessful)
                    {
                        responseInterface.onResponseSuccess("",
                            getResponseFromBody(response!!.body()!!, clazz = AssetsResponse::class.java))
                    }
                    else
                    {
                        responseInterface.onResponseError("", baseResponse.httpCode, baseResponse.code, baseResponse.msg)
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<ResponseBody>?, t: Throwable?)
            {
                responseInterface.onResponseError("", HttpResponseCodes.Default,
                    respMessage = context.getString(R.string.alert_sorry_something_went_wrong))
            }
        })
    }

    fun callTowerDetail(context: Context, strAuthToken : String,id : String, responseInterface: ResponseInterface)
    {
        val call = WebClient.getApiClient().getTowerDetail(id,strAuthToken)
        call.enqueue(object : retrofit2.Callback<ResponseBody>
        {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?)
            {
                val baseResponse = checkIsSuccessResponse(response, context)
                if (baseResponse != null)
                {
                    if (baseResponse.isSuccessful)
                    {
                        responseInterface.onResponseSuccess("",
                            getResponseFromBody(response!!.body()!!, clazz = TowerDetailResponse::class.java))
                    }
                    else
                    {
                        responseInterface.onResponseError("", baseResponse.httpCode, baseResponse.code, baseResponse.msg)
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<ResponseBody>?, t: Throwable?)
            {
                responseInterface.onResponseError("", HttpResponseCodes.Default,
                    respMessage = context.getString(R.string.alert_sorry_something_went_wrong))
            }
        })
    }

    fun callWeatherDetail(context: Context, responseInterface: ResponseInterface)
    {
        val extraParam = "weather?lat=${AppPreferences.getDouble(AppPreferences.LAT,0.0,context)}&lon=${AppPreferences.getDouble(AppPreferences.LON,0.0,context)}&APPID=33dbed84413b1d3c31c7b83421297ee5"

//        val url = WsConst.WEATHER_BASE_URL + WsConst.extraParam
        val url = WsConst.WEATHER_BASE_URL + extraParam

        val call = WebClient.getWeatherApiClient().getWeatherData(url)
        call.enqueue(object : retrofit2.Callback<ResponseBody>
        {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?)
            {
                val baseResponse = checkIsSuccessResponse(response, context)
                if (baseResponse != null)
                {
                    if (baseResponse.isSuccessful)
                    {
                        responseInterface.onResponseSuccess("",
                            getResponseFromBody(response!!.body()!!, clazz = WeatherResponse::class.java))
                    }
                    else
                    {
                        responseInterface.onResponseError("", baseResponse.httpCode, baseResponse.code, baseResponse.msg)
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<ResponseBody>?, t: Throwable?)
            {
                responseInterface.onResponseError("", HttpResponseCodes.Default,
                    respMessage = context.getString(R.string.alert_sorry_something_went_wrong))
            }
        })
    }

    fun callSensorList(context: Context, strAuthToken : String, id : String, responseInterface: ResponseInterface)
    {
        val call = WebClient.getApiClient().getSensorList(id,strAuthToken)
        call.enqueue(object : retrofit2.Callback<ResponseBody>
        {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?)
            {
                val baseResponse = checkIsSuccessResponse(response, context)
                if (baseResponse != null)
                {
                    if (baseResponse.isSuccessful)
                    {
                        responseInterface.onResponseSuccess("",
                            getResponseFromBody(response!!.body()!!, clazz = SensorListResponse::class.java))
                    }
                    else
                    {
                        responseInterface.onResponseError("", baseResponse.httpCode, baseResponse.code, baseResponse.msg)
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<ResponseBody>?, t: Throwable?)
            {
                responseInterface.onResponseError("", HttpResponseCodes.Default,
                    respMessage = context.getString(R.string.alert_sorry_something_went_wrong))
            }
        })
    }


    fun callDevicestatus(context: Context, strAuthToken : String, id : String, status : StatusRequestBean, responseInterface: ResponseInterface)
    {
        val call = WebClient.getApiClient().getDeviceStatus(status ,id, strAuthToken)
        call.enqueue(object : retrofit2.Callback<ResponseBody>
        {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?)
            {
                val baseResponse = checkIsSuccessResponse(response, context)
                if (baseResponse != null)
                {
                    if (baseResponse.isSuccessful)
                    {
                        responseInterface.onResponseSuccess("",
                            getResponseFromBody(response!!.body()!!, clazz = DeviceStatusResponse::class.java))
                    }
                    else
                    {
                        responseInterface.onResponseError("", baseResponse.httpCode, baseResponse.code, baseResponse.msg)
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<ResponseBody>?, t: Throwable?)
            {
                responseInterface.onResponseError("", HttpResponseCodes.Default,
                    respMessage = context.getString(R.string.alert_sorry_something_went_wrong))
            }
        })
    }

    fun callDeviceAlertList(context: Context, strAuthToken : String, responseInterface: ResponseInterface)
    {
        val call = WebClient.getApiClient().getDeviceAlertList(strAuthToken)
        call.enqueue(object : retrofit2.Callback<ResponseBody>
        {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?)
            {
                val baseResponse = checkIsSuccessResponse(response, context)
                if (baseResponse != null)
                {
                    if (baseResponse.isSuccessful)
                    {
                        responseInterface.onResponseSuccess("",
                            getResponseFromBody(response!!.body()!!, clazz = DeviceAlertsResponse::class.java))
                    }
                    else
                    {
                        responseInterface.onResponseError("", baseResponse.httpCode, baseResponse.code, baseResponse.msg)
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<ResponseBody>?, t: Throwable?)
            {
                responseInterface.onResponseError("", HttpResponseCodes.Default,
                    respMessage = context.getString(R.string.alert_sorry_something_went_wrong))
            }
        })
    }

}