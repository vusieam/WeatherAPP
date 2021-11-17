package co.sz.vusieam.mobileweathertest.models.linkedentities

import androidx.room.Embedded
import androidx.room.Relation
import co.sz.vusieam.mobileweathertest.models.entities.ExtendedWeatherEntity
import co.sz.vusieam.mobileweathertest.models.entities.WeatherEntity

class WeatherAndExtendedWeather(
    @Embedded
    val weather: WeatherEntity,
    @Relation(parentColumn = "w_internalID", entityColumn = "ew_weatherID")
    val extendedWeather: ExtendedWeatherEntity
) {
}