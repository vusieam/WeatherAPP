package co.sz.vusieam.mobileweathertest.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "TblWeather"
, indices = [Index(value = ["w_internalID"], name ="idx_ew_internalID")])
class WeatherEntity(
    @PrimaryKey() @ColumnInfo(name = "w_internalID") val internalID:String,
    @ColumnInfo(name = "w_temperature") val temperature : Int,
    @ColumnInfo(name = "w_temperature_feel") val temperature_feel : Int,
    @ColumnInfo(name = "w_temperature_min") val temperature_min : Int,
    @ColumnInfo(name = "w_temperature_max") val temperature_max : Int,
    @ColumnInfo(name = "w_humidity") val humidity : Int,
    @ColumnInfo(name = "w_description") val description : String,
    @ColumnInfo(name = "w_date") val date : String
) {
}


