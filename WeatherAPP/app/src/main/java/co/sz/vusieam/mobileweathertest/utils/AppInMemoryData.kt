package co.sz.vusieam.mobileweathertest.utils

import androidx.room.ColumnInfo
import co.sz.vusieam.mobileweathertest.models.LocationMarkerModel
import co.sz.vusieam.mobileweathertest.models.entities.*
import co.sz.vusieam.mobileweathertest.views.activities.MainActivity

object AppInMemoryData {
    var liveWeatherInfo: LiveWeatherInformation? = null
    var liveCityInfo: LiveCityInformation? = null
    var liveExtendedWeatherData: ArrayList<LiveExtendedWeatherInformation>? = null
    var locationMarkers: ArrayList<LocationMarkerModel>? = null
    var activity: MainActivity? = null
}