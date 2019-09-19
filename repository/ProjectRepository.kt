package com.mvvm.demo.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.mvvm.demo.service.model.Project

class ProjectRepository {

//    private var apiInterface : APIInterface ?= null

    companion object {

        private var projectRepository : ProjectRepository? = null
        @Synchronized
        @JvmStatic
        fun  getInstance():ProjectRepository
        {
            if(projectRepository == null)
                projectRepository = ProjectRepository()
            return projectRepository!!
        }
    }


    fun projectList(userId : String) : LiveData<List<Project>>
    {
        val projectData = MutableLiveData<List<Project>>()

        CallWebService.projectList(userId,object : WebServiceResponseListener{
            override fun onResponseSuccess(request: Any, response: MutableLiveData<List<Project>>) {

                /*Type Cast from Response to List<Project>*/
                /*if(response is List<*>)
                    projectData.value = response.filterIsInstance<Project>()*/

                projectData.value = response.value

                /*if(response is Project)
                    projectData.value = response*/
            }

            override fun onFailedResponse(errorCode: Int, message: String) {
                projectData.value = null
            }

            override fun onFailedResponse(s: String) {
                projectData.value = null
            }

        })
        return projectData
    }

    fun projectDetails(userId : String, projectName : String) : LiveData<Project>
    {
        var liveData = CallWebService.projectDetail(userId,projectName)

        return liveData
    }

}