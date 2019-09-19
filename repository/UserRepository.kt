package com.mvvm.demo.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.Update
import android.content.Context
import android.os.AsyncTask
import com.mvvm.demo.service.room.model.User
import com.mvvm.demo.service.room.model.UserDatabase

class UserRepository {

    /*Static block*/
    companion object {

        var userRepository : UserRepository? = null
        var content : Context? = null

        fun getInstance(application: Application) : UserRepository
        {
            this.content = application
            if(userRepository == null) userRepository = UserRepository()
            return userRepository!!
        }
    }

    fun userRegistration(user:User)
            /*Uncomment below code for Return newly inserted User*/
            /* : LiveData<User> */
    {

        RegisterDataTask(user).execute()


        var regUser = MutableLiveData<User>()

        regUser.value = user


        // TODO : Below code is for Update user to say that Insert record is Successfull

        /*var regUser = MutableLiveData<User>()
//        var user: User? = null

//        var insertResult : Long = MyApp.database.userDao().insertUser(user)
        var insertResult : Long = UserDatabase.getInstance(content!!)!!.userDao().insertUser(user)

        regUser.value = user

        return regUser*/

    }

    class RegisterDataTask(var user : User) : AsyncTask<Void,Void, Long>(){

        private var userVar : User = user

        private var result : Long = 0

        override fun doInBackground(vararg p0: Void?): Long? {

            var result : Long = UserDatabase.getInstance(content!!)!!.userDao().insertUser(userVar)
            return result
        }

        override fun onPostExecute(result: Long?) {
            super.onPostExecute(result)


            var regUser = MutableLiveData<User>()

            regUser.value = user

            this.result = result!!

        }
    }

    fun loginUser(user:User) : LiveData<User>
    {
        var loginUser = MutableLiveData<User>()

        var user = UserDatabase.getInstance(content!!)!!.userDao().getUser(user.email,user.password)

        loginUser.value = user

        return loginUser
    }

}