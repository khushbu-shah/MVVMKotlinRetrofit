package com.cell_tower.utils

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.cell_tower.R


object DialogUtils {

    var alertDialog: Dialog? = null

    var independentAlertDialog: Dialog? = null

    var progressDialog: ProgressDialog? = null

    /**
     * Display message according to your parameters
     *
     * @param mContext Context
     * @param title Title of alert dialog
     * @param message Message of alert dialog
     * @param positiveString Positive button text to display. It should not null or blank
     * @param positiveClick Positive button click listener
     * @param negativeString Negative button text to display. It should not null or blank
     * @param negativeClick Negative button click listener
     */
    fun displayAlert(mContext: Context,
                     title: String,
                     message: String,
                     positiveString: String? = null,
                     positiveClick: DialogInterface.OnClickListener? = null,
                     negativeString: String? = null,
                     negativeClick: DialogInterface.OnClickListener? = null)
    {
        val dialogBuilder = AlertDialog.Builder(mContext, R.style.MyDialogTheme)

        if (title == null)
        {
            throw NullPointerException(mContext.getString(R.string.alert_title_required))
            return
        }
        if (message == null)
        {
            throw NullPointerException(mContext.getString(R.string.alert_message_required))
            return
        }

        val titleText = SpannableString(title)


        titleText.setSpan(ForegroundColorSpan(Color.BLUE), 0, titleText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        dialogBuilder.setTitle(titleText)
        dialogBuilder.setCancelable(false)
        dialogBuilder.setMessage(message)
        if (positiveString != null && positiveClick != null)
        {
            if (positiveString.trim().isEmpty())
            {
                throw NullPointerException(mContext.getString(R.string.alert_positive_text_required))
                return
            }
            dialogBuilder.setPositiveButton(positiveString, positiveClick)

        }

        if (negativeString != null && negativeClick != null)
        {
            if (negativeString.trim().isEmpty())
            {
                throw NullPointerException(mContext.getString(R.string.alert_negative_text_required))
                return
            }
            dialogBuilder.setNegativeButton(negativeString, negativeClick)
        }

        if (positiveString == null && negativeString == null)
        {
            if (positiveClick == null && negativeClick == null)
            {
                dialogBuilder.setPositiveButton(mContext.getString(R.string.str_ok)) { dialog, which ->
                    run {
                        dialog.dismiss()
                    }
                }
            }
        }
        if (alertDialog != null && alertDialog!!.isShowing) alertDialog!!.dismiss()

        alertDialog = dialogBuilder.create()

        if (alertDialog != null)
        {
            alertDialog!!.show()
        }
        else {
            (alertDialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE)
            (alertDialog as AlertDialog).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLUE)
        }
    }

    /**
     * Display message according to your parameters
     *
     * @param mContext Context
     * @param title Title of alert dialog
     * @param message Message of alert dialog
     * @param positiveString Positive button text to display. It should not null or blank
     * @param positiveClick Positive button click listener
     * @param negativeString Negative button text to display. It should not null or blank
     * @param negativeClick Negative button click listener
     */
    fun displayAlertIndependent(mContext: Context,
                                title: String,
                                message: String,
                                positiveString: String? = null,
                                positiveClick: DialogInterface.OnClickListener? = null,
                                negativeString: String? = null,
                                negativeClick: DialogInterface.OnClickListener? = null)
    {
        val dialogBuilder = AlertDialog.Builder(mContext, R.style.MyDialogTheme)

        if (title == null)
        {
            throw NullPointerException(mContext.getString(R.string.alert_title_required))
            return
        }
        if (message == null)
        {
            throw NullPointerException(mContext.getString(R.string.alert_message_required))
            return
        }

        val titleText = SpannableString(title)
        titleText.setSpan(ForegroundColorSpan(Color.BLUE), 0, titleText.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        dialogBuilder.setTitle(titleText)
        dialogBuilder.setCancelable(false)
        dialogBuilder.setMessage(message)
        if (positiveString != null && positiveClick != null)
        {
            if (positiveString.trim().isEmpty())
            {
                throw NullPointerException(mContext.getString(R.string.alert_positive_text_required))
                return
            }
            dialogBuilder.setPositiveButton(positiveString, positiveClick)
        }

        if (negativeString != null && negativeClick != null)
        {
            if (negativeString.trim().isEmpty())
            {
                throw NullPointerException(mContext.getString(R.string.alert_negative_text_required))
                return
            }
            dialogBuilder.setNegativeButton(negativeString, negativeClick)
        }

        if (positiveString == null && negativeString == null)
        {
            if (positiveClick == null && negativeClick == null)
            {
                dialogBuilder.setPositiveButton(mContext.getString(R.string.str_ok)) { dialog, which ->
                    run {
                        dialog.dismiss()
                    }
                }
            }
        }
        if (independentAlertDialog != null && independentAlertDialog!!.isShowing) independentAlertDialog!!.dismiss()

        independentAlertDialog = dialogBuilder.create()

        if (independentAlertDialog != null)
        {
            independentAlertDialog!!.show()
        }

        (independentAlertDialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE)
        (independentAlertDialog as AlertDialog).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLUE)
    }

    /**
     * Display Progress Dialog with custom subject and message
     *
     * @param mContext Context
     * @param subject Title of alert dialog
     * @param message Message of alert dialog
     *
     */
    fun showLoadingDialog(mContext: Context)
    {
        val mProgressView = View.inflate(mContext, R.layout.layout_loading_dialog, null)
        val pbProgress = mProgressView.findViewById(R.id.imgLoader) as View

        alertDialog = Dialog(mContext, R.style.ThemeDialogCustom)
        alertDialog!!.setCancelable(false)
        alertDialog!!.setContentView(mProgressView)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                alertDialog!!.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                alertDialog!!.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
            else
            {
                alertDialog!!.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }

            // Set properties
            mProgressView.fitsSystemWindows = false
        }

        if (alertDialog != null && alertDialog!!.isShowing)
        {
            alertDialog!!.dismiss()
        }

        if (alertDialog != null)
        {
            alertDialog!!.show()
        }
    }

    /**
     * Display Progress Dialog with custom subject and message
     *
     * @param mContext Context
     * @param subject Title of alert dialog
     * @param message Message of alert dialog
     *
     */
    fun showProgressDialog(mContext: Activity)
    {
        progressDialog = ProgressDialog(mContext)

        progressDialog!!.setCancelable(false)
        progressDialog!!.max = 100
        progressDialog!!.isIndeterminate = false
        progressDialog!!.progress = 0
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)

        //        alertDialog!!.setContentView(mProgressView)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                progressDialog!!.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                progressDialog!!.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
            else
            {
                progressDialog!!.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }

            // Set properties
            //            mProgressView.fitsSystemWindows = false;
        }

        if (progressDialog != null && progressDialog!!.isShowing) progressDialog!!.dismiss()

        progressDialog!!.show()
    }

    /**
     * Hide lash show dialog
     */
    fun hideDialog()
    {
        if (alertDialog != null && alertDialog!!.isShowing)
        {
            alertDialog!!.dismiss()
        }
        if (progressDialog != null && progressDialog!!.isShowing)
        {
            progressDialog!!.dismiss()
        }
    }

    fun isLoadingDialogShowing(): Boolean = alertDialog != null && alertDialog!!.isShowing

    fun isProgressDialogShowing(): Boolean = progressDialog != null && progressDialog!!.isShowing


    fun showSnackbar(view: View, text: String, length: Int)
    {
        val snackbar = Snackbar.make(view, text, length)
        snackbar.view.findViewById<TextView>(android.support.design.R.id.snackbar_text).maxLines = 3
        snackbar.show()
    }

}