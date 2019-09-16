package com.cell_tower.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle

object ActivityUtils
{
    fun <T : Any?> startNewActivity(mContext: Context, data: Bundle? = null, clazz: Class<T>)
    {
        val intent = Intent(mContext, clazz)
        if (data != null)
            intent.putExtras(data)
        mContext.startActivity(intent)
    }

    fun <T : Any?> startNewActivityWithFinish(mActivity: Activity, data: Bundle? = null, clazz: Class<T>)
    {
        val intent = Intent(mActivity, clazz)
        if (data != null)
            intent.putExtras(data)
        mActivity.startActivity(intent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            mActivity.finishAfterTransition()
        }
        else
        {
            mActivity.finish()
        }
    }

    fun <T : Any?> startLoginActivityWithFinishAll(mActivity: Activity, data: Bundle? = null, clazz: Class<T>)
    {
        val intent = Intent(mActivity, clazz)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        if (data != null)
            intent.putExtras(data)
        mActivity.startActivity(intent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            mActivity.finishAfterTransition()
        }
        else
        {
            mActivity.finish()
        }
    }
}