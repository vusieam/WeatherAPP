package co.sz.vusieam.mobileweathertest.models

import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all") val all : Int
)
