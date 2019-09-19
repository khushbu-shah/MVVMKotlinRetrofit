package com.mvvm.demo.app

import android.app.Application
import com.mvvm.demo.service.room.model.UserDatabase

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        /*MyApp.database= Room.databaseBuilder(this, UserDatabase::class.java, "users.db").allowMainThreadQueries()
                .build()*/

        UserDatabase.getInstance(this)
    }
    companion object {
         lateinit var database:  UserDatabase
    }
}