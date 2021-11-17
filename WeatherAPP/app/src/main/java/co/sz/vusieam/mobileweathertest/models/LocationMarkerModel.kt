package co.sz.vusieam.mobileweathertest.models

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng

class LocationMarkerModel {
    lateinit var latLong: LatLng
    lateinit var title:String
    lateinit var markerIcon: Bitmap

    constructor(latLong: LatLng, title: String, markerIcon: Bitmap){
        this.latLong = latLong
        this.title = title
        this.markerIcon = markerIcon
    }

    constructor(){}
}