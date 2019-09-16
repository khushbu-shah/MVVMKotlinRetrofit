package com.cell_tower.utils

import android.content.Context
import android.content.SharedPreferences
import com.cell_tower.webservices.responsepojos.LoginResponse
import com.google.gson.Gson
import kotlin.contracts.contract


object AppPreferences {

    private val TAG = AppPreferences::class.java.simpleName
    private const val PREFERENCE_FILE_NAME = "application_preferences"
    public const val USER_INFO = "USER_INFO"
    public const val AUTH_TOKEN = "AUTH_TOKEN"
    public const val EMAIl = "EMAIl"
    public const val PASSWORD = "PASSWORD"
    public const val LAT = "LATITUDE"
    public const val LON = "LONGITUDE"

    private fun getSharedPreferences(context: Context): SharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_NAME, 0)

    private fun getSharedPreferencesEditor(context: Context): SharedPreferences.Editor = getSharedPreferences(context).edit()

    fun getBoolean(key: String, defaultValue: Boolean, context: Context): Boolean = getSharedPreferences(context).getBoolean(key, defaultValue)

    fun getInt(key: String, defaultValue: Int, context: Context): Int = getSharedPreferences(context).getInt(key, defaultValue)

    fun setInt(key: String, value: Int, context: Context) {
        getSharedPreferencesEditor(context).putInt(key, value).apply()
    }

    fun getDouble(key: String, defaultValue: Double, context: Context): Double {
        return java.lang.Double.longBitsToDouble(getSharedPreferences(context).getLong(key, 0))
//        return if (!prefs.contains(key)) defaultValue
//        else java.lang.Double.longBitsToDouble(prefs.getLong(key, 0))

    }
    fun setDouble(key: String, value: Double, context: Context) {
        getSharedPreferencesEditor(context).putLong(key, java.lang.Double.doubleToRawLongBits(value)).apply()
    }

    @JvmStatic
    fun getString(key: String, defaultValue: String, context: Context): String? = getSharedPreferences(context).getString(key, defaultValue)

    fun setBoolean(key: String, value: Boolean, context: Context) {
        getSharedPreferencesEditor(context).putBoolean(key, value).apply()
    }

    fun setString(key: String, value: String?, context: Context) {
        getSharedPreferencesEditor(context).putString(key, value).apply()
    }

    fun clearAllPreferences(context: Context) {
        getSharedPreferencesEditor(context).clear().apply()
    }

    fun saveUserInfo(userInfo: LoginResponse.UserInfo?, context: Context) {
        if (userInfo != null) {
            val gson = Gson()
            val jsonObject = gson.toJson(userInfo)
            setString(USER_INFO, jsonObject, context)
        } else {
            setString(USER_INFO, null, context)
        }
    }

    fun getUserInfo(context: Context): LoginResponse.UserInfo? {
        try {
            val gson = Gson()
            val json = getString(USER_INFO, "", context)
            return gson.fromJson<LoginResponse.UserInfo>(json, LoginResponse.UserInfo::class.java)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return null
    }
}

