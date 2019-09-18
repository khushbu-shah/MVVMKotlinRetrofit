package com.cell_tower.webservices.responsepojos

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.cell_tower.BR

class LoginResponse ()  : BaseObservable() {

    val status: Int? = null
    val data: Data? = null

    class Data() : BaseObservable()
    {
        val message: String? = null
        val access_token: String? = null
        val expires_in: Int? = null
        val refresh_token: String? = null
        val userInfo: UserInfo? = null
    }

    class UserInfo() : BaseObservable()
    {

        var id: String? = null

        /*Add @Bindable for Two way Binding*/

        var firstName: String = ""
            @Bindable get
            set(value) {
                field = value
                notifyPropertyChanged(BR.firstName)
            }

        var lastName: String = ""
            @Bindable get
            set(value) {
                field = value
                notifyPropertyChanged(BR.lastName)
            }

        var email: String = ""
            @Bindable get
            set(value) {
                field = value
                notifyPropertyChanged(BR.email)

            }

        var location: String = ""
            @Bindable get
            set(value) {
                field = value
                notifyPropertyChanged(BR.location)

            }

        var phoneNumber: String = ""
            @Bindable get
            set(value) {
                field = value
                notifyPropertyChanged(BR.phoneNumber)

            }

        val status: String? = null
    }
}



