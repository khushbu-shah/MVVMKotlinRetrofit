package com.cell_tower.webservices.requestpojos

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.cell_tower.BR

class ChangePasswordRequest () : BaseObservable()
{
    var currentpassword: String = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.currentpassword)
        }

    var newpassword: String = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.newpassword)
        }

    var confirmpassword: String = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.confirmpassword)
        }
}