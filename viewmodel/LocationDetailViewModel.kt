package com.cell_tower.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.cell_tower.utils.NetworkUtils
import com.cell_tower.webservices.enums.HttpResponseCodes
import com.cell_tower.webservices.interfaces.response.ResponseInterface
import com.cell_tower.webservices.responsepojos.*
import com.cell_tower.webservices.responsepojos.WeatherPojo.WeatherResponse
import com.cell_tower.webservices.servicemodel.ServiceCall

class LocationDetailViewModel(application: Application) : AndroidViewModel(application) {

    var loadingStatus = MutableLiveData<Boolean>()

    var mApplication = application

    var locationDetailResponse = MutableLiveData<LocationDetailResponse>()
    var assetsResponse = MutableLiveData<AssetsResponse>()
    var alertListResponse = MutableLiveData<AlertListResponse>()
    var errorResponse = MutableLiveData<String>()
    var weatherResponse = MutableLiveData<WeatherResponse>()


    fun setIsLoading(loading: Boolean) {
        loadingStatus.postValue(loading)
    }

    fun setSuccess(response: LocationDetailResponse) {
        locationDetailResponse.postValue(response)
        setIsLoading(false)
    }

    fun setSuccess(response: WeatherResponse) {
        weatherResponse.postValue(response)
        setIsLoading(false)
    }

    fun setSuccess(response: AssetsResponse) {
        assetsResponse.postValue(response)
        setIsLoading(false)
    }

    fun setSuccess(response: AlertListResponse) {
        alertListResponse.postValue(response)
        setIsLoading(false)
    }

    fun setErrorSuccess(response: String) {
        errorResponse.postValue(response)
        setIsLoading(false)
    }

    public fun callLocationDetail(strAuthToken: String, id: String) {
        if (NetworkUtils.isNetworkAvailable(mApplication)) {

            ServiceCall.callLocationDetail(mApplication, strAuthToken, id, object : ResponseInterface {
                override fun onResponseSuccess(request: Any, response: Any) {

                    setIsLoading(false)

                    /* if (request is DashboardCountResponse)
                     {*/
                    if (response is LocationDetailResponse) {
                        setSuccess(response)
                    }
//                    }
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
        } else {
//            DialogUtils.displayAlert(this, getString(R.string.app_name), getString(R.string.alert_no_internet_connection))
        }
    }

    public fun callAlertList(strAuthToken: String, locationId: String) {
        if (NetworkUtils.isNetworkAvailable(mApplication)) {

            ServiceCall.callAlertList(mApplication, locationId,strAuthToken, object : ResponseInterface {
                override fun onResponseSuccess(request: Any, response: Any) {

                    setIsLoading(false)

//                    if (request is DashboardCountResponse) {
                        if (response is AlertListResponse) {
                            setSuccess(response)
//                        }
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
        } else {
        }
    }


    public fun callAssetList(strAuthToken: String, id: String) {
        if (NetworkUtils.isNetworkAvailable(mApplication)) {

            ServiceCall.callAssetList(mApplication, strAuthToken, id, object : ResponseInterface {
                override fun onResponseSuccess(request: Any, response: Any) {

                    setIsLoading(false)

                    /* if (request is DashboardCountResponse)
                     {*/
                    if (response is AssetsResponse) {
                        setSuccess(response)
                    }
//                    }
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
        } else {
//            DialogUtils.displayAlert(this, getString(R.string.app_name), getString(R.string.alert_no_internet_connection))
        }
    }

    public fun callWeatherData() {
        if (NetworkUtils.isNetworkAvailable(mApplication)) {

            ServiceCall.callWeatherDetail(mApplication,  object : ResponseInterface {
                override fun onResponseSuccess(request: Any, response: Any) {

                    setIsLoading(false)

                    if (response is WeatherResponse) {
                        setSuccess(response)
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
        } else {
//            DialogUtils.displayAlert(this, getString(R.string.app_name), getString(R.string.alert_no_internet_connection))
        }
    }

}