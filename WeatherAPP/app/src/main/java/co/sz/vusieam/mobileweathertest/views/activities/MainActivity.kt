package co.sz.vusieam.mobileweathertest.views.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import co.sz.vusieam.mobileweathertest.R
import co.sz.vusieam.mobileweathertest.models.LocationMarkerModel
import co.sz.vusieam.mobileweathertest.models.entities.CityEntity
import co.sz.vusieam.mobileweathertest.models.entities.ExtendedWeatherEntity
import co.sz.vusieam.mobileweathertest.models.entities.WeatherEntity
import co.sz.vusieam.mobileweathertest.services.data.WeatherDatabaseService
import co.sz.vusieam.mobileweathertest.utils.AppBasicConstants
import co.sz.vusieam.mobileweathertest.utils.AppInMemoryData
import co.sz.vusieam.mobileweathertest.utils.DVTHelperFunctions
import co.sz.vusieam.mobileweathertest.viewmodels.WeatherViewModel
import co.sz.vusieam.mobileweathertest.views.fragments.Favourites
import co.sz.vusieam.mobileweathertest.views.fragments.FavouritesMapsFragment
import co.sz.vusieam.mobileweathertest.views.fragments.MissingPermissionsFragment
import co.sz.vusieam.mobileweathertest.views.fragments.TodayWeather
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var addToFav:ImageView
    private lateinit var toogle:ActionBarDrawerToggle
    private lateinit var todayWeatherFragment:TodayWeather
    private lateinit var savedWeatherFragment:Favourites


    private lateinit var viewModel: WeatherViewModel

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
        .get(WeatherViewModel::class.java)
        initialiseComponents()
        AppInMemoryData.activity = this
        checkLocationService()
    }


    /**
     * Initialising all the controls/components in this activity so they ready to be used.
     */
    private fun initialiseComponents(){
        addToFav = findViewById(R.id.imgAddToFavourites)
        addToFav.setOnClickListener {
            //implement the adding of the current temperature information to database.
            try{
                //create direct database instance
                if(AppInMemoryData.liveWeatherInfo == null && AppInMemoryData.liveCityInfo == null && AppInMemoryData.liveExtendedWeatherData == null){
                    Toast.makeText(applicationContext, "There is not enough weather information to add to favourites.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val db = WeatherDatabaseService.getDatabaseInstance(this)
                val UID = DVTHelperFunctions.generateUID()
                val weatherEntity = WeatherEntity(
                    UID,
                    AppInMemoryData.liveWeatherInfo!!.temperature,
                    AppInMemoryData.liveWeatherInfo!!.temperature_feel,
                    AppInMemoryData.liveWeatherInfo!!.temperature_min,
                    AppInMemoryData.liveWeatherInfo!!.temperature_max,
                    AppInMemoryData.liveWeatherInfo!!.humidity,
                    AppInMemoryData.liveWeatherInfo!!.description,
                    AppInMemoryData.liveWeatherInfo!!.date)

                Log.d("DVTViewModel", "WeatherEntity: ${Gson().toJson(weatherEntity)}")
                runBlocking{
                    db.getWeatherServiceDao().insertWeather(weatherEntity)
                }

                Log.d("DVTViewModel", "CityInfo: ${Gson().toJson(AppInMemoryData.liveCityInfo)}")

                val city = CityEntity(UID,
                    AppInMemoryData.liveCityInfo!!.cityID,
                    AppInMemoryData.liveCityInfo!!.cityName,
                    AppInMemoryData.liveCityInfo!!.cityGeoLatitude,
                    AppInMemoryData.liveCityInfo!!.cityGeoLongitude,
                    AppInMemoryData.liveCityInfo!!.cityCountry)
                Log.d("DVTViewModel", "CityEntity: ${Gson().toJson(city)}")

                runBlocking{
                    db.getWeatherServiceDao().insertCity(city)
                }

                val extWeatherList = arrayListOf<ExtendedWeatherEntity>()
                AppInMemoryData.liveExtendedWeatherData!!.forEach {
                    item ->
                    extWeatherList.add(ExtendedWeatherEntity(UID,
                    item.temperature,
                    item.temperature_feel,
                    item.temperature_min,
                    item.temperature_max,
                    item.humidity,
                    item.description,
                    item.dayOfWeek,
                    item.date,
                    item.position))
                }
                if(extWeatherList.isNotEmpty()){
                    runBlocking{
                        db.getWeatherServiceDao().insertBulkExtendedWeather(extWeatherList)
                    }
                }
                Toast.makeText(applicationContext, "Added to favourites", Toast.LENGTH_SHORT).show()
            }
            catch(ex:Exception) {
                Log.d("DVTViewModel", "DVTViewModelException: ${Gson().toJson(ex)}")
                Toast.makeText(applicationContext, "Internal error", Toast.LENGTH_SHORT).show()
            }

        }
        setSupportActionBar(toolbar)
        toogle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.nav_open, R.string.nav_close)
        toogle.isDrawerIndicatorEnabled = true
        nav_menu_view.setNavigationItemSelectedListener(this)
        drawer_layout.addDrawerListener(toogle)
        toogle.syncState()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        if(toogle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }


    /**
     * When any of the navigation menu option is selected, then this function is invoked.
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_home -> {
                drawer_layout.closeDrawer(GravityCompat.START)
                if (!canAccessLocation() || !canAccessCoreLocation()){
                    Toast.makeText(this, "The app requires permissions to access this function", Toast.LENGTH_SHORT).show()
                    return false
                }
                addToFav.visibility = View.VISIBLE
                setToolBarTittle("Today Weather")
                todayWeatherFragment = TodayWeather()
                changeFragment(todayWeatherFragment)
            }
            R.id.nav_favourites ->{
                drawer_layout.closeDrawer(GravityCompat.START)
                viewModel.allWeatherAndCity.observe(this, Observer { list ->
                    list.let {
                        if(it.isNullOrEmpty()){
                            viewModel.allWeatherAndCity.removeObservers(this)
                            Toast.makeText(this, "There are currently no favourite weathers", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            addToFav.visibility = View.GONE
                            setToolBarTittle("Favourites")
                            savedWeatherFragment = Favourites()
                            changeFragment(savedWeatherFragment)
                        }
                    }
                })
            }


            R.id.nav_close_app -> {
                finishAndRemoveTask()

                /*val pid = Process.myPid()
                Process.killProcess(pid)*/
            }

            R.id.nav_maps -> {
                drawer_layout.closeDrawer(GravityCompat.START)
                viewModel.allWeatherAndCity.observe(this, Observer { list ->
                    list.let {
                        if(it.isNullOrEmpty()){
                            viewModel.allWeatherAndCity.removeObservers(this)
                            Toast.makeText(this, "There are currently no favourite weathers", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            AppInMemoryData.locationMarkers = arrayListOf()
                            it.forEach {
                                    weather ->
                                val marker = LocationMarkerModel()
                                marker.latLong = LatLng(weather.city.cityGeoLatitude.toDouble() ,weather.city.cityGeoLongitude.toDouble())
                                marker.title = weather.city.cityName
                                AppInMemoryData.locationMarkers!!.add(marker)
                            }

                            addToFav.visibility = View.GONE
                            setToolBarTittle("Favourites on Map")
                            val favouritesMapsFragment = FavouritesMapsFragment()
                            changeFragment(favouritesMapsFragment)
                        }
                    }
                })
            }
        }
        return true
    }


    /**
     * Setting or changing the toolbar title.
     */
    private fun setToolBarTittle(title:String){
        supportActionBar?.title = title
    }


    /**
     * Function for changing fragment so that all reusable code is centralized.
     */
    private fun changeFragment(frag: Fragment){
        val fragment = supportFragmentManager.beginTransaction()
        fragment.replace(R.id.fragment_container, frag).commit()
    }


    //region Location service checking functions

    /**
     * Function to check if the location service is enabled. If enabled then all is good.
     * If not, ask the user for permission so the app can access location services.
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkLocationService(){
        try{
            if (!canAccessLocation() || !canAccessCoreLocation()) {
                requestPermissions(AppBasicConstants.ARRAY_PERMISSIONS, AppBasicConstants.PERMISSION_REQUEST_CODE);
            } else {
                initialiseComponents()
                setToolBarTittle("Today Weather")
                todayWeatherFragment = TodayWeather()
                todayWeatherFragment.activity = this
                AppInMemoryData.activity = this
                changeFragment(todayWeatherFragment)
            }
        }
        catch(ex:Exception){
            Log.d("checkLocationService", "exception: ${Gson().toJson(ex)}")
        }
    }

    /**
     * Check permission to access fine location.
     */
    private fun canAccessLocation(): Boolean {
        return hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    /**
     * Check permission to access coarse location.
     */
    private fun canAccessCoreLocation(): Boolean {
        return hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    /**
     * Confirm/check if the specific permission has been granted.
     */
    private fun hasPermission(perm: String): Boolean {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm)
    }

    /**
     * Override function for requested permission results.
     */
    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            AppBasicConstants.PERMISSION_REQUEST_CODE ->{
                if(canAccessLocation() && canAccessCoreLocation()){
                    setToolBarTittle("Today Weather")
                    todayWeatherFragment = TodayWeather()
                    changeFragment(todayWeatherFragment)
                }
                else{
                    drawer_layout.closeDrawer(GravityCompat.START)
//                    addToFav = findViewById(R.id.imgAddToFavourites)
                    imgAddToFavourites.visibility = View.GONE
                    setToolBarTittle("Missing Permissions")
                    changeFragment(MissingPermissionsFragment())
                }
            }
        }
    }
    //endregion
}