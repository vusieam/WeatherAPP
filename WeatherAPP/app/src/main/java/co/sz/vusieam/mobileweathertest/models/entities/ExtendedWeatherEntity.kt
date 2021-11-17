package co.sz.vusieam.mobileweathertest.models.entities

import androidx.room.*

@Entity(tableName = "TblExtendedWeather",
foreignKeys = [ForeignKey(entity = WeatherEntity::class,
    parentColumns = arrayOf("w_internalID"), childColumns = arrayOf("ew_weatherID"), onDelete = ForeignKey.CASCADE)],
    indices = [Index(value = ["ew_weatherID"], name ="idx_ew_w_internalID")])
class ExtendedWeatherEntity (
    @ColumnInfo(name = "ew_weatherID") var weatherID:String,
    @ColumnInfo(name = "ew_temperature") val temperature : Int,
    @ColumnInfo(name = "ew_temperature_feel") val temperature_feel : Int,
    @ColumnInfo(name = "ew_temperature_min") val temperature_min : Int,
    @ColumnInfo(name = "ew_temperature_max") val temperature_max : Int,
    @ColumnInfo(name = "ew_humidity") val humidity : Int,
    @ColumnInfo(name = "ew_description") val description : String,
    @ColumnInfo(name = "ew_dayOfWeek") val dayOfWeek : String,
    @ColumnInfo(name = "ew_date") val date : String,
    @ColumnInfo(name = "ew_position") val position : Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ew_internalID")
    var internalID = 0
}


class LiveExtendedWeatherInformation{
    var temperature : Int = 0
    var temperature_feel : Int = 0
    var temperature_min : Int = 0
    var temperature_max : Int = 0
    var  humidity : Int = 0
    lateinit var  description : String
    lateinit var dayOfWeek : String
    lateinit var date : String
    var position : Int = 0

    constructor(
        temperature: Int,
        temperature_feel: Int,
        temperature_min: Int,
        temperature_max: Int,
        humidity: Int,
        description: String,
        dayOfWeek: String,
        date: String,
        position: Int
    ) {
        this.temperature = temperature
        this.temperature_feel = temperature_feel
        this.temperature_min = temperature_min
        this.temperature_max = temperature_max
        this.humidity = humidity
        this.description = description
        this.dayOfWeek = dayOfWeek
        this.date = date
        this.position = position
    }

    constructor(){}
}