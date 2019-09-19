package com.mvvm.demo.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import com.mvvm.demo.repository.UserRepository
import com.mvvm.demo.service.room.model.User

class UserViewModel( application: Application) : AndroidViewModel(application) {

//    var user : User? = null

    override fun onCleared() {
        super.onCleared()
    }

    fun regUser(user : User) /*: LiveData<User>*/
    {
        /*return*/ UserRepository.getInstance(getApplication()).userRegistration(user)
    }

    fun loginUser(user: User) : LiveData<User>
    {
        return UserRepository.getInstance(getApplication()).loginUser(user)
    }

    class Factory(val application: Application) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return UserViewModel(application) as T
        }
    }

}