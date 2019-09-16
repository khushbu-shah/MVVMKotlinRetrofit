package com.cell_tower.webservices.requestpojos

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.cell_tower.BR

data class LoginRequest (
    var email: String,
    var password: String? = null
) : BaseObservable()
{

    var emailM : String
    @Bindable get() = email
    set(value){
        email = value
        notifyPropertyChanged(BR.emailM)
    }

    var passwordM : String
    @Bindable get() = password!!
    set(value){
        password = value
        notifyPropertyChanged(BR.passwordM)
    }
    @Bindable
    fun isValid(): Boolean {
        var valid = isEmailValid(false)
        valid = isPasswordValid(false) && valid
        return valid
    }

    fun isEmailValid(setMessage: Boolean): Boolean {
        // Minimum a@b.c
        if (email != null && email.length > 5) {
            val indexOfAt = email.indexOf("@")
            val indexOfDot = email.lastIndexOf(".")
            if (indexOfAt in 1 until indexOfDot && indexOfDot < email.length - 1) {
//                emailError.set(null)
                return true
            } else {
                if (setMessage)
//                    emailError.set(R.string.error_format_invalid)
                return false
            }
        }
        /*if (setMessage)
            emailError.set(R.string.error_too_short)*/
        return false
    }

    fun isPasswordValid(setMessage: Boolean): Boolean {
        if (password != null && password!!.length > 5) {
//            passwordError.set(null)
            return true
        } else {
            /*if (setMessage)
                passwordError.set(R.string.error_too_short)*/
            return false
        }
    }
}