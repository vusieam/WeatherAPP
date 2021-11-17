package co.sz.vusieam.mobileweathertest.services.gpslocation

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder


open class DVTGpsLocationService(private val context: Context) : Service(), LocationListener//, IntentService(IDENTIFIER)
{

    private val locationManager: LocationManager? = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private lateinit var location: Location

    @SuppressLint("MissingPermission")
    fun getLocation(provider: String?): Location? {
        if (locationManager!!.isProviderEnabled(provider.toString())) {
            if (provider != null) {
                locationManager.requestLocationUpdates(
                    provider,
                    MIN_TIME_FOR_UPDATE,
                    MIN_DISTANCE_FOR_UPDATE.toFloat(), this
                )
            }
            location = provider?.let { locationManager.getLastKnownLocation(it) }!!
            return location
        }
        return null
    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

//    override fun onHandleIntent(p0: Intent?) {
//        TODO("Not yet implemented")
//    }

    override fun onLocationChanged(p0: Location) {
        TODO("Not yet implemented")
    }


    companion object {
        private const val MIN_DISTANCE_FOR_UPDATE: Long = 10
        private const val MIN_TIME_FOR_UPDATE = 1000 * 60 * 2.toLong()
        private const val IDENTIFIER = "GetAddressIntentService"
    }

}