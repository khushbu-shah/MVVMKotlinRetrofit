package com.cell_tower.webservices.responsepojos

data class AlertListResponse(
    var data: Data = Data(),
    var status: Int = 0
)
{
    data class Data(
        var locationTower: ArrayList<LocationTower> = ArrayList<LocationTower>()
    )
    {
        data class LocationTower(
            var createdAt: String = "",
            var ruleName: String = "",
            var sensorName: String = "",
            var deviceName: String = "",
            var sensorValue: String = "",
            var id: String = "",
            var status: String = "",
            var dayDiff: Int=0,
            var isDisplay : Boolean = true

        )
    }
}