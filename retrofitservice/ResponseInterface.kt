package com.cell_tower.webservices.interfaces.response

import com.cell_tower.webservices.enums.HttpResponseCodes

internal interface ResponseInterface{

    fun onResponseSuccess(request: Any, response: Any)

    fun onResponseError(request: Any, httpCode: HttpResponseCodes, resCode: String? = null, respMessage: String)

}