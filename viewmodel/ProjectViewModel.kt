package com.mvvm.demo.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.databinding.ObservableField
import com.mvvm.demo.repository.ProjectRepository
import com.mvvm.demo.service.model.Project
import java.util.*

class ProjectViewModel(application : Application, projectId : String) : AndroidViewModel(application) {

    private val projectObserval = ProjectRepository.getInstance().projectDetails("Google",projectId)

    var project :ObservableField<Project> = ObservableField()

    fun setProject(project: Project)
    {
        this.project.set(project)
    }

    fun getObservableProject() : LiveData<Project>
    {
        return projectObserval
    }

    class Factory(private val application: Application, private val projectID: String) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return ProjectViewModel(application, projectID) as T
        }
    }

}