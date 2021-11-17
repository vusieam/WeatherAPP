package co.sz.vusieam.mobileweathertest.models.entities

import androidx.room.*

@Entity(tableName ="TblCities",
    foreignKeys = [ForeignKey(entity = WeatherEntity::class,
        parentColumns = arrayOf("w_internalID"), childColumns = arrayOf("c_weatherID"), onDelete = ForeignKey.CASCADE)],
    indices = [Index(value = ["c_weatherID"], name ="idx_c_w_internalID")
        , Index(value = ["C_name"], name ="idx_c_cityName")
        , Index(value = ["C_country"], name ="idx_c_cityCountry")])
class CityEntity (
    @ColumnInfo(name = "c_weatherID") var weatherID:String,
    @ColumnInfo(name = "C_id") val cityID : Int,
    @ColumnInfo(name = "C_name") val cityName : String,
    @ColumnInfo(name = "C_latitude") val cityGeoLatitude : String,
    @ColumnInfo(name = "C_longitude") val cityGeoLongitude : String,
    @ColumnInfo(name = "C_country") val cityCountry : String
        ){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "c_internalID")
    var internalID = 0
}


class LiveCityInformation{
    var cityID : Int = 0
    lateinit var cityName : String
    lateinit var cityGeoLatitude : String
    lateinit var cityGeoLongitude : String
    lateinit var cityCountry : String

    constructor(cityID:Int, cityName:String, cityGeoLatitude:String, cityGeoLongitude : String, cityCountry : String ){
        this.cityID = cityID
        this.cityName = cityName
        this.cityGeoLatitude = cityGeoLatitude
        this.cityGeoLongitude = cityGeoLongitude
        this.cityCountry = cityCountry
    }

    constructor(){}
}


class LiveWeatherInformation{
    var temperature:Int = 0
    var temperature_feel:Int = 0
    var temperature_min:Int = 0
    var temperature_max:Int = 0
    var humidity:Int = 0
    lateinit var description:String
    lateinit var date:String

    constructor(temperature:Int, temperature_feel:Int, temperature_min:Int, temperature_max:Int, humidity:Int, description:String, date:String ){
        this.temperature = temperature
        this.temperature_feel = temperature_feel
        this.temperature_min = temperature_min
        this.temperature_max = temperature_max
        this.humidity = humidity
        this.description = description
        this.date = date
    }

    constructor(){}
}