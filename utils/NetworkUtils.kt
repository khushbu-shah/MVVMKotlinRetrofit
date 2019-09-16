package com.cell_tower.utils

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtils {

    /***
     * Function that checks if network is available
     *
     * @param context passed by calling activity
     * @return true if network available otherwise false
     */
    fun isNetworkAvailable(context: Context): Boolean
    {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (activeNetwork != null)
        {
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI)
            {
                // connected to wifi
                return true
            }
            else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
            {// connected to the mobile provider's data plan
                return true
            }
            else if (activeNetwork.type == ConnectivityManager.TYPE_ETHERNET)
            {// connected to the ethernet
                return true
            }
        }
        return false
    }

}