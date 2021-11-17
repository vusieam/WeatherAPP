package co.sz.vusieam.mobileweathertest.models

import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("id") val id : Int,
    @SerializedName("main") var main : String,
    @SerializedName("description") val description : String,
    @SerializedName("icon") val icon : String
)
