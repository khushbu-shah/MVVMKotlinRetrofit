package com.mvvm.demo.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.mvvm.demo.repository.ProjectRepository
import com.mvvm.demo.service.model.Project

/*Error :: Cannot create an instance of class ViewModel*/
/* Note : Make sure your ViewModel has constructor with only one parameter i.e. Application.*/

class ProjectListViewModel(application : Application) : AndroidViewModel(application) {

    private val projectObserval = ProjectRepository.getInstance().projectList("Google")

    fun getObservableProject() : LiveData<List<Project>>
    {
        return projectObserval
    }

}