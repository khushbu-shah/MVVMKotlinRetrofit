package com.cell_tower.webservices.enums

enum class HttpResponseCodes(var httpCode: Int) {

    Success(200), NoContent(204), BadRequest(400), UnAuthorized(401),
    SiteDisabled(403), NotFound(404), MethodNotAllowed(405), NotAcceptable(406),
    RequestTimeOut(408), Conflict(409),
    PredicationFailed(412), ServerError(500), ForceLogout(101), Default(-1);

    companion object
    {
        fun findCode(httpCode: Int): HttpResponseCodes
        {
            return values().find { httpResponseCodes -> httpResponseCodes.httpCode == httpCode } ?: Default
        }
    }

}