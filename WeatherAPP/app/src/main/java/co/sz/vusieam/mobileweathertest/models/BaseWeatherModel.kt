package co.sz.vusieam.mobileweathertest.models

import com.google.gson.annotations.SerializedName

data class BaseWeatherModel(
    @SerializedName("cod") val cod : String,
    @SerializedName("message") val message : String,
    @SerializedName("cnt") val cnt : Double,
    @SerializedName("list") val list : List<ListWeatherModel>,
    @SerializedName("city") val city : City
)