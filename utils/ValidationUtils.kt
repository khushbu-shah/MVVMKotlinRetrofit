package com.esdglobal.nf151.utils

import java.util.regex.Pattern

object ValidationUtils
{
    fun isValidEmail(target: CharSequence?): Boolean
    {
        if (!(target == null || target.isEmpty()))
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()) return true
        return false
    }

    fun isValidPassword(password: CharSequence): Boolean
    {
        val PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!&-_{}\\[\\]*|+=]).{6,})"
        val pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }

    fun isValidMobile(phone: String): Boolean
            = android.util.Patterns.PHONE.matcher(phone).matches()
}