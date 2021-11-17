package co.sz.vusieam.mobileweathertest.models.linkedentities

import androidx.room.Embedded
import androidx.room.Relation
import co.sz.vusieam.mobileweathertest.models.entities.CityEntity
import co.sz.vusieam.mobileweathertest.models.entities.WeatherEntity

class WeatherAndCityLinked(
    @Embedded
    val weather: WeatherEntity,
    @Relation(parentColumn = "w_internalID", entityColumn = "c_weatherID")
    val city: CityEntity
) {

}