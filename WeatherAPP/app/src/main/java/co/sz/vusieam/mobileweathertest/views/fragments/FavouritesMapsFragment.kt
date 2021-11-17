package co.sz.vusieam.mobileweathertest.views.fragments

import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.sz.vusieam.mobileweathertest.R
import co.sz.vusieam.mobileweathertest.utils.AppInMemoryData

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class FavouritesMapsFragment : Fragment() {

    private lateinit var googleMap: GoogleMap

    private val callback = OnMapReadyCallback { gMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        /*val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
        googleMap = gMap
        drawLocationMarker()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favourites_maps, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


    private fun createGeoAddress(latLong:LatLng): String{
        val geoCoder = Geocoder(requireActivity(), Locale.getDefault())
        val address = geoCoder.getFromLocation(latLong.latitude, latLong.longitude,1)
        return address[0].getAddressLine(0).toString()
    }
    private fun drawLocationMarker(){
        AppInMemoryData.locationMarkers!!.forEach {
            data ->
            googleMap.addMarker(MarkerOptions()
                .position(data.latLong)
                //.anchor(0.2f, 0.2f)
                .title(data.title)
                .snippet(createGeoAddress(data.latLong)))
//            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15f))
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(data.latLong))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(data.latLong, 7f))
            /*val marker = MarkerOptions().position(data.latLong).title(data.title)
                .snippet(createGeoAddress(data.latLong))
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(data.latLong))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(data.latLong, 15f))
            googleMap.addMarker(marker)*/
        }


    }
}