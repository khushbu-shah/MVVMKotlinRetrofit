package com.cell_tower.common

object WsConst {

    //Dev-DD
    // var BASE_URL: String = "http://192.168.3.222:3010/api/"

    //Dev-RP
    //var BASE_URL: String = "http://192.168.3.75:3010/api/"

    //Live
    var BASE_URL: String = "http://celltoweriotcenteral.softwebopensource.com/api/"

    var WEATHER_BASE_URL: String = "http://api.openweathermap.org/data/2.5/"

    public val extraParam = "weather?q=Ahmedabad&APPID=ea574594b9d36ab688642d5fbeab847e"

    object ResponseCodes
    {
        val SUCCESS_RESPONSE = IntRange(200, 220)
        val BAD_REQUEST = IntRange(400, 420)
        val OPERATIONAL_ERROR = IntRange(500, 520)
    }

}