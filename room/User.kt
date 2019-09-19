package com.mvvm.demo.service.room.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.databinding.Bindable
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry
import com.mvvm.demo.BR

@Entity(tableName = "user")
 class User :Observable

    /*val userName: String="",
    @PrimaryKey
    val email: String="",
    val mobile: String="",
    val password: String=""*/

{
    @Ignore
    val propertyChangeRegistry = PropertyChangeRegistry()


    //    var userName: String=""
    var userName: String = ""
    @Bindable get
    set(value) {
        field = value
        propertyChangeRegistry.notifyChange(this, BR.userName)
    }


    @PrimaryKey
    var email: String=""
    @Bindable get
    set(value) {
        field = value
        propertyChangeRegistry.notifyChange(this, BR.email)
    }
    var mobile: String=""
    @Bindable get
    set(value) {
        field = value
        propertyChangeRegistry.notifyChange(this,BR.mobile)
    }

    var password: String=""
    @Bindable get
    set(value) {
        field = value
        propertyChangeRegistry.notifyChange(this, BR.password)
    }

    @Ignore
    var remember: Boolean=false
    @Bindable get
    set(value) {
        field = value
        propertyChangeRegistry.notifyChange(this, BR.remember)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        propertyChangeRegistry.remove(callback)
    }
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        propertyChangeRegistry.add(callback)
    }

    /*var userName: String = ""
        @Bindable get
        set(value) {
            field = value
            propertyChangeRegistry.notifyChange(this, BR.userName)
        }*/


    /*@Ignore
    constructor() : this("", "", "", "")*/
}