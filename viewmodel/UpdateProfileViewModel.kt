package com.cell_tower.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.widget.Toast
import com.cell_tower.utils.NetworkUtils
import com.cell_tower.webservices.enums.HttpResponseCodes
import com.cell_tower.webservices.interfaces.response.ResponseInterface
import com.cell_tower.webservices.requestpojos.ChangePasswordRequest
import com.cell_tower.webservices.responsepojos.ChangePwdResponse
import com.cell_tower.webservices.responsepojos.UpdateProfileResponse
import com.cell_tower.webservices.responsepojos.LoginResponse
import com.cell_tower.webservices.servicemodel.ServiceCall

class UpdateProfileViewModel(application: Application) : AndroidViewModel(application) {

    var loadingStatus = MutableLiveData<Boolean>()

    var mApplication = application

    var profileResponse =  MutableLiveData<UpdateProfileResponse>()
    var changePwdResponse =  MutableLiveData<ChangePwdResponse>()
    var profileErrorResponse =  MutableLiveData<String>()
    var passwordNotMatchResponse =  MutableLiveData<String>()

    fun setIsLoading(loading: Boolean) {
        loadingStatus.postValue(loading)
    }

    fun setSuccess(response: UpdateProfileResponse) {
        profileResponse.postValue(response)
        setIsLoading(false)
    }

    fun setSuccess(response: ChangePwdResponse) {
        changePwdResponse.postValue(response)
        setIsLoading(false)
    }

    fun setErrorSuccess(response: String) {
        profileErrorResponse.postValue(response)
        setIsLoading(false)
    }

    fun setPasswordNotMatchError(response: String) {
        passwordNotMatchResponse.postValue(response)
        setIsLoading(false)
    }

    public fun callWsProfile(userInfo: LoginResponse.UserInfo, strAuthToken : String)
    {
        if (NetworkUtils.isNetworkAvailable(mApplication)) {

            ServiceCall.callUpdateUser(mApplication, userInfo, strAuthToken, object : ResponseInterface
            {
                override fun onResponseSuccess(request: Any, response: Any) {

                    setIsLoading(false)

                    if (request is LoginResponse.UserInfo)
                    {
                        if (response is UpdateProfileResponse)
                        {
                            setSuccess(response)
                        }
                    }
                }

                override fun onResponseError(
                    request: Any,
                    httpCode: HttpResponseCodes,
                    resCode: String?,
                    respMessage: String
                ) {
                    setIsLoading(false)
                    setErrorSuccess(respMessage)
                }
            })
        }
        else {
//            DialogUtils.displayAlert(this, getString(R.string.app_name), getString(R.string.alert_no_internet_connection))
        }
    }

    public fun callChangePwd(changePWDRequest: ChangePasswordRequest, strAuthToken : String)
    {
        if(changePWDRequest.confirmpassword.equals(changePWDRequest.newpassword))
        {
            if(!changePWDRequest.currentpassword.equals(changePWDRequest.newpassword))
            {
                if (NetworkUtils.isNetworkAvailable(mApplication)) {

                    ServiceCall.callChangePwd(mApplication, changePWDRequest, strAuthToken, object : ResponseInterface
                    {
                        override fun onResponseSuccess(request: Any, response: Any) {

                            setIsLoading(false)

                            if (request is ChangePasswordRequest)
                            {
                                if (response is ChangePwdResponse)
                                {
                                    setSuccess(response)
                                }
                            }
                        }

                        override fun onResponseError(
                            request: Any,
                            httpCode: HttpResponseCodes,
                            resCode: String?,
                            respMessage: String
                        ) {
                            setIsLoading(false)
                            setErrorSuccess(respMessage)
                        }
                    })
                }
                else {
//            DialogUtils.displayAlert(this, getString(R.string.app_name), getString(R.string.alert_no_internet_connection))
                }
            }
            else
            {
                setPasswordNotMatchError("Old Password and New password can not be same.!!")
//            invalidPwd()
            }
        }
        else
        {
            setPasswordNotMatchError("New and Confirm Password must be same.!!")
//            invalidPwd()
        }

    }

    public fun invalidPwd()
    {
        Toast.makeText(mApplication,"Password must be same.!!",Toast.LENGTH_SHORT).duration
    }

}