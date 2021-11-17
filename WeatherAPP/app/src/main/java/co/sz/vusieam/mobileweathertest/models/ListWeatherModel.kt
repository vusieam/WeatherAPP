package co.sz.vusieam.mobileweathertest.models

import com.google.gson.annotations.SerializedName

data class ListWeatherModel(
    @SerializedName("dt") val dt : Long,
    @SerializedName("main") val main : Main,
    @SerializedName("weather") val weather : List<Weather>,
    @SerializedName("clouds") val clouds : Clouds,
    @SerializedName("wind") val wind : Wind,
    @SerializedName("visibility") val visibility : Int,
    @SerializedName("pop") val pop : Double,
    @SerializedName("sys") val sys : Sys,
    @SerializedName("dt_txt") val dt_txt : String
)
