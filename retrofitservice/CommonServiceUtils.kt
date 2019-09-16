package com.cell_tower.webservices.servicemodel

import android.content.Context
import com.cell_tower.R
import com.cell_tower.common.WsConst
import com.cell_tower.webservices.enums.HttpResponseCodes
import com.cell_tower.webservices.responsepojos.BaseResponse
import com.cell_tower.webservices.responsepojos.ErrorResponse
import com.google.gson.Gson
import okhttp3.ResponseBody
import java.lang.reflect.Type

internal object CommonServiceUtils
{
    fun <T> getResponseFromBody(response: ResponseBody, clazz: Class<T>): Any
    {
        val stringResponse = response.string().toString()
        return Gson().fromJson(stringResponse, clazz)!!
    }

    fun getResponseFromBody(response: ResponseBody, type: Type): Any
    {
        val stringResponse = response.string().toString()
        return Gson().fromJson(stringResponse, type)
    }

    fun checkIsSuccessResponse(response: retrofit2.Response<ResponseBody>?, mContext: Context): BaseResponse?
    {
        if (response != null)
        {
            if (response.isSuccessful && WsConst.ResponseCodes.SUCCESS_RESPONSE.contains(response.code()))
            {
                return BaseResponse(true, HttpResponseCodes.findCode(response.code()), "", "")
            }
            else if (!response.isSuccessful && WsConst.ResponseCodes.BAD_REQUEST.contains(response.code()))
            {
                return try
                {
                    val res = response.errorBody()
                    if (res != null && !res.toString().isBlank())
                    {
                        val errorRes = getResponseFromBody(res, ErrorResponse::class.java) as ErrorResponse
                        BaseResponse(false, HttpResponseCodes.findCode(response.code()), errorRes.code, errorRes.msg)
                    }
                    else
                    {
                        BaseResponse(false, HttpResponseCodes.findCode(response.code()), HttpResponseCodes.findCode(response.code()).name,
                                     mContext.getString(R.string.alert_sorry_something_went_wrong))
                    }
                }
                catch (ex: Exception)
                {
                    BaseResponse(false, HttpResponseCodes.findCode(response.code()), HttpResponseCodes.findCode(response.code()).name,
                                 mContext.getString(R.string.alert_sorry_something_went_wrong))
                }
            }
            else if (!response.isSuccessful && WsConst.ResponseCodes.OPERATIONAL_ERROR.contains(response.code()))
            {
                return try
                {
                    val res = response.errorBody()
                    if (res != null && !res.toString().isBlank())
                    {
                        val errorRes = getResponseFromBody(res, ErrorResponse::class.java) as ErrorResponse
                        BaseResponse(false, HttpResponseCodes.findCode(response.code()), errorRes.code, errorRes.msg)
                    }
                    else
                    {
                        BaseResponse(false, HttpResponseCodes.findCode(response.code()), HttpResponseCodes.findCode(response.code()).name,
                                     mContext.getString(R.string.alert_sorry_something_went_wrong))
                    }
                }
                catch (ex: Exception)
                {
                    BaseResponse(false, HttpResponseCodes.findCode(response.code()), HttpResponseCodes.findCode(response.code()).name,
                                 mContext.getString(R.string.alert_sorry_something_went_wrong))
                }
            }
        }
        return null
    }
}