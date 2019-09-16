package com.cell_tower.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.cell_tower.webservices.responsepojos.AlertListResponse
import com.cell_tower.webservices.responsepojos.DeviceAlertsResponse
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object CommonUtils {

    fun hideSoftKeyboard(view: View?)
    {
        try
        {
            if (view != null)
            {
                view.clearFocus()
                val inputMethodManager = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }

    }

    fun getDayDifference(item: AlertListResponse.Data.LocationTower): Int {
        var str = changeDateFormat(item.createdAt,"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'","yyyy-MM-dd HH:mm:ss")

        var strSplit = str!!.split(" ")

        var strDate = strSplit[0]

        val currentTime = Calendar.getInstance().time

        val format = SimpleDateFormat("yyyy-MM-dd")

        var dateCurrent = format.format(currentTime)
        var dateResponse = format.parse(strDate)

        val diff = format.parse(dateCurrent).time - dateResponse.time

        val diffSeconds = diff / 1000 % 60
        val diffMinutes = diff / (60 * 1000) % 60
        val diffHours = diff / (60 * 60 * 1000) % 24
        val diffDays: Int = (diff / (24 * 60 * 60 * 1000)).toInt()

        var result: String = when (diffDays) {
            0 -> "Today"
            1 -> "$diffDays Day Ago"
            2 -> "$diffDays Day Ago"
            3 -> "$diffDays Day Ago"
            4 -> "$diffDays Day Ago"
            5 -> "$diffDays Day Ago"
            6 -> "$diffDays Day Ago"
            else -> "$diffDays is Older"
        }

        return diffDays
    }

    fun getDayDifference(item: DeviceAlertsResponse.Data.DeviceAlerts): Int {

        var str = changeDateFormat(item.createdAt,"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'","yyyy-MM-dd HH:mm:ss")

        var strSplit = str!!.split(" ")

        var strDate = strSplit[0]

        val currentTime = Calendar.getInstance().time

        val format = SimpleDateFormat("yyyy-MM-dd")

        var dateCurrent = format.format(currentTime)
        var dateResponse = format.parse(strDate)

        val diff = format.parse(dateCurrent).time - dateResponse.time

        val diffSeconds = diff / 1000 % 60
        val diffMinutes = diff / (60 * 1000) % 60
        val diffHours = diff / (60 * 60 * 1000) % 24
        val diffDays: Int = (diff / (24 * 60 * 60 * 1000)).toInt()

        var result: String = when (diffDays) {
            0 -> "Today"
            1 -> "$diffDays Day Ago"
            2 -> "$diffDays Day Ago"
            3 -> "$diffDays Day Ago"
            4 -> "$diffDays Day Ago"
            5 -> "$diffDays Day Ago"
            6 -> "$diffDays Day Ago"
            else -> "$diffDays is Older"
        }

        return diffDays
    }


    /**
     * this will convert the strDate format and return string
     *
     * @param inputDate
     * @param inputDateFormat
     * @param outputDateFormat
     * @return
     */
    fun changeDateFormat(inputDate: String, inputDateFormat: String, outputDateFormat: String): String? {
        // String inputPattern = "dd-MMM-yyyy";
        // String outputPattern = "yyyy-MM-dd";
        //06 18 04 01 14 29 01 8A 02
        // year month date hh:mm:ss miliseconds
        val inputFormat = SimpleDateFormat(inputDateFormat)
        val outputFormat = SimpleDateFormat(outputDateFormat)
        var date: Date? = null
        var str: String? = null

        try {
            date = inputFormat.parse(inputDate)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return str
    } //end of parseDate
}