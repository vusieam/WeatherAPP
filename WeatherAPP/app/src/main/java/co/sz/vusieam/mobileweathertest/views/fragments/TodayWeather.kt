package co.sz.vusieam.mobileweathertest.views.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import co.sz.vusieam.mobileweathertest.R
import co.sz.vusieam.mobileweathertest.models.BaseWeatherModel
import co.sz.vusieam.mobileweathertest.models.ListWeatherModel
import co.sz.vusieam.mobileweathertest.models.entities.LiveCityInformation
import co.sz.vusieam.mobileweathertest.models.entities.LiveExtendedWeatherInformation
import co.sz.vusieam.mobileweathertest.models.entities.LiveWeatherInformation
import co.sz.vusieam.mobileweathertest.utils.*
import co.sz.vusieam.mobileweathertest.views.activities.MainActivity
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_menu_header.*
import java.text.SimpleDateFormat
import java.util.*


class TodayWeather : Fragment() {
    private lateinit var todayWeatherFragment: ConstraintLayout
    lateinit var imageViewWeatherIcon: ImageView
    lateinit var tvCurrentTemperature: TextView
    lateinit var tvCurrentTempInWord: TextView
    lateinit var tvDayTempMin: TextView
    private lateinit var tvDayTempCurrent: TextView
    private lateinit var tvDayTempMax: TextView
    lateinit var tvCityName: TextView

    public lateinit var activity:MainActivity

    //region 5 days weather temperature
    private lateinit var tvDayOneWeekDay: TextView
    private lateinit var imgDayOneTempIcon: ImageView
    private lateinit var tvDayOneTempValue: TextView

    private lateinit var tvDayTwoWeekDay: TextView
    private lateinit var imgDayTwoTempIcon: ImageView
    private lateinit var tvDayTwoTempValue: TextView

    private lateinit var tvDayThreeWeekDay: TextView
    private lateinit var imgDayThreeTempIcon: ImageView
    private lateinit var tvDayThreeTempValue: TextView

    private lateinit var tvDayFourWeekDay: TextView
    private lateinit var imgDayFourTempIcon: ImageView
    private lateinit var tvDayFourTempValue: TextView

    private lateinit var tvDayFiveWeekDay: TextView
    private lateinit var imgDayFiveTempIcon: ImageView
    private lateinit var tvDayFiveTempValue: TextView

    //endregion

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var requestQueue: RequestQueue? = null

    //region days weathers container
    private lateinit var baseModel:BaseWeatherModel
    //endregion

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_today_weather, container, false)
        initialiseControls(view)
        requestQueue = Volley.newRequestQueue(requireActivity())
        checkLocationService()

        return view
    }


    private fun initialiseControls(view:View){
        try{
            todayWeatherFragment = view.findViewById(R.id.todayWeatherFragment)
            imageViewWeatherIcon = view.findViewById(R.id.imageViewWeatherIcon)
            imageViewWeatherIcon.post{imageViewWeatherIcon.setImageBitmap(null)}

            tvCurrentTemperature = view.findViewById(R.id.tvCurrentTemperature)
            tvCurrentTemperature.post{tvCurrentTemperature.text = ""}

            tvCurrentTempInWord = view.findViewById(R.id.tvCurrentTempInWord)
            tvCurrentTempInWord.post{tvCurrentTempInWord.text = ""}

            tvDayTempMin = view.findViewById(R.id.tvDayTempMin)
            tvDayTempMin.post{tvDayTempMin.text = ""}

            tvDayTempCurrent = view.findViewById(R.id.tvDayTempCurrent)
            tvDayTempCurrent.post{tvDayTempCurrent.text = ""}

            tvDayTempMax = view.findViewById(R.id.tvDayTempMax)
            tvDayTempMax.post{tvDayTempMax.text = ""}

            tvCityName = view.findViewById(R.id.tvCityName)
            tvCityName.post{tvCityName.text = ""}

            //Five day weather display controls
            tvDayOneWeekDay = view.findViewById(R.id.tvDayOneWeekDay)
            tvDayOneWeekDay.post{tvDayOneWeekDay.text = ""}

            imgDayOneTempIcon = view.findViewById(R.id.imgDayOneTempIcon)
            imgDayOneTempIcon.post{imgDayOneTempIcon.setImageBitmap(null)}

            tvDayOneTempValue = view.findViewById(R.id.tvDayOneTempValue)
            tvDayOneTempValue.post{tvDayOneTempValue.text = ""}


            tvDayTwoWeekDay = view.findViewById(R.id.tvDayTwoWeekDay)
            tvDayTwoWeekDay.post{tvDayTwoWeekDay.text = ""}

            imgDayTwoTempIcon = view.findViewById(R.id.imgDayTwoTempIcon)
            imgDayTwoTempIcon.post{imgDayTwoTempIcon.setImageBitmap(null)}

            tvDayTwoTempValue = view.findViewById(R.id.tvDayTwoTempValue)
            tvDayTwoTempValue.post{tvDayTwoTempValue.text = ""}


            tvDayThreeWeekDay = view.findViewById(R.id.tvDayThreeWeekDay)
            tvDayThreeWeekDay.post{tvDayThreeWeekDay.text = ""}

            imgDayThreeTempIcon = view.findViewById(R.id.imgDayThreeTempIcon)
            imgDayThreeTempIcon.post{imgDayThreeTempIcon.setImageBitmap(null)}

            tvDayThreeTempValue = view.findViewById(R.id.tvDayThreeTempValue)
            tvDayThreeTempValue.post{tvDayThreeTempValue.text = ""}


            tvDayFourWeekDay = view.findViewById(R.id.tvDayFourWeekDay)
            tvDayFourWeekDay.post{tvDayFourWeekDay.text = ""}

            imgDayFourTempIcon = view.findViewById(R.id.imgDayFourTempIcon)
            imgDayFourTempIcon.post{imgDayFourTempIcon.setImageBitmap(null)}

            tvDayFourTempValue = view.findViewById(R.id.tvDayFourTempValue)
            tvDayFourTempValue.post{tvDayFourTempValue.text = ""}


            tvDayFiveWeekDay = view.findViewById(R.id.tvDayFiveWeekDay)
            tvDayFiveWeekDay.post{tvDayFiveWeekDay.text = ""}

            imgDayFiveTempIcon = view.findViewById(R.id.imgDayFiveTempIcon)
            imgDayFiveTempIcon.post{imgDayFiveTempIcon.setImageBitmap(null)}

            tvDayFiveTempValue = view.findViewById(R.id.tvDayFiveTempValue)
            tvDayFiveTempValue.post{tvDayFiveTempValue.text = ""}
        }
        catch(ex:Exception){
            Log.d("initialiseControls", "exception: ${Gson().toJson(ex)}")
        }
    }



    //region Location Functions

    @SuppressLint("MissingPermission")
    private fun initLocationService(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                try{
                    if(location != null){
                        val locLatitude = location.latitude.toString()
                        val locLongitude = location.longitude.toString()
                        Log.d("dvtLocation","Location latitude: ${locLatitude}, longitude: $locLongitude")
                        getWeatherUsingVolley(locLatitude, locLongitude)
                    }
                }
                catch(ex:Exception){
                    LoadingDialogs.hideDialog()
                    Log.d("initLocationService", "exception: ${Gson().toJson(ex)}")
                }
            }
            .addOnFailureListener {
                LoadingDialogs.hideDialog()
                Log.d("initLocationService", "exception: ${Gson().toJson(it)}")
            }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkLocationService(){
        try{
            LoadingDialogs.showDialog(requireActivity(), false)
            if (!canAccessLocation() || !canAccessCoreLocation()) {
                requireActivity().requestPermissions(AppBasicConstants.ARRAY_PERMISSIONS, AppBasicConstants.PERMISSION_REQUEST_CODE);
            } else {
                initLocationService()
            }
        }
        catch(ex:Exception){
            LoadingDialogs.hideDialog()
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

    private fun hasPermission(perm: String): Boolean {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(requireActivity(), perm)
    }

    //endregion


    @SuppressLint("SimpleDateFormat")
    private fun getWeatherUsingVolley(locLatitude:String, locLongitude:String){
        try{
            AppInMemoryData.liveCityInfo = null
            AppInMemoryData.liveWeatherInfo = null
            AppInMemoryData.liveExtendedWeatherData = null
            val apiURL = ApiEndpointConstants.getWeatherByGeoCoordinates(locLatitude, locLongitude)

            val stringRequest: StringRequest = StringRequest(apiURL, {
                stringResponse ->
                try{
                    val gsonBuilder = GsonBuilder()
                    val gson = gsonBuilder.create()
                    val model = gson.fromJson(stringResponse, BaseWeatherModel::class.java)

                    if(model != null){
                        baseModel = model
                        if(model.cod == "200"){
                            AppInMemoryData.liveCityInfo = LiveCityInformation()
                            AppInMemoryData.liveWeatherInfo = LiveWeatherInformation()
                            AppInMemoryData.liveExtendedWeatherData = arrayListOf()

                            tvCityName.post{tvCityName.text = model.city.name}
                            AppInMemoryData.liveCityInfo?.cityID = model.city.id
                            AppInMemoryData.liveCityInfo?.cityName = model.city.name
                            AppInMemoryData.liveCityInfo?.cityCountry = model.city.country
                            AppInMemoryData.liveCityInfo!!.cityGeoLongitude = locLongitude //model.city.coord.lat.toString()
                            AppInMemoryData.liveCityInfo?.cityGeoLatitude = locLatitude //model.city.coord.lon.toString()

                            //sorting the list so that we can start extracting temperature readings
                            val tempList:ArrayList<ListWeatherModel> = arrayListOf()
                            tempList.addAll(model.list)
                            tempList.sortBy { it-> it.dt_txt}
                            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(tempList[0].dt_txt)
                            val apiResponseDate = format.format(date!!)
                            val todayDate = format.format(Date())
                            if(!todayDate.equals(apiResponseDate, true)){
                                Log.d("stringResponse", "Today temperature reading temporarily not available")
                            }
                            else{
                                extractTodayTemperatureReadings(tempList)
                            }


                            //extract five days weather readings
                            extractFiveDaysTemperatureReadings(tempList, 1)
                            extractFiveDaysTemperatureReadings(tempList, 2)
                            extractFiveDaysTemperatureReadings(tempList, 3)
                            extractFiveDaysTemperatureReadings(tempList, 4)
                            extractFiveDaysTemperatureReadings(tempList, 5)
                            LoadingDialogs.hideDialog()
                        }
                        else{
                            Log.d("stringResponse", "Request for weather unsuccessful with response message ${model.message}")
                            LoadingDialogs.hideDialog()
                        }
                    }
                    else{
                        Log.d("stringResponse", "No data received from request")
                        LoadingDialogs.hideDialog()
                    }
                }
                catch(ex:Exception){
                    Log.d("stringResponse", "exception: ${Gson().toJson(ex)}")
                    LoadingDialogs.hideDialog()
                }
            }, {
                errorResponse ->
                Log.d("stringResponse", "exception: ${Gson().toJson(errorResponse)}")
                LoadingDialogs.hideDialog()
            })
            requestQueue?.add(stringRequest)
        }
        catch(ex:Exception){
            Log.d("stringResponse", "exception: ${Gson().toJson(ex)}")
        }
    }


    @SuppressLint("ResourceType")
    private fun extractTodayTemperatureReadings(tempList:ArrayList<ListWeatherModel>){
        try{
            if(tempList.isNullOrEmpty())
                return
            var newTempList = arrayListOf<ListWeatherModel>()
            tempList.forEach { temp ->
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(temp.dt_txt)
                val apiResponseDate = format.format(date!!)
                val todayDate = format.format(Date())
                if(todayDate.equals(apiResponseDate)){
                    newTempList.add(temp)
                }
            }
            if(newTempList.isNotEmpty()){
                //Sort the arrayList by ascending order of the minimum temperature value to get the minimum temperature available from the list
                newTempList.sortBy { v -> v.main.temp_min }
                tvDayTempMin.text = getString(
                    R.string.degree_symbol,
                    newTempList[0].main.temp_min.toString().substring(0, 2)
                )
                AppInMemoryData.liveWeatherInfo!!.temperature_min = Integer.parseInt(newTempList[0].main.temp_min.toString().substring(0, 2))

                //Sort the arrayList by descending order of the maximum temperature value to get the maximum temperature available from the list
                newTempList.sortByDescending { v -> v.main.temp_max }
                tvDayTempMax.text = getString(
                    R.string.degree_symbol,
                    newTempList[0].main.temp_max.toString().substring(0, 2)
                )
                /*imageViewWeatherIcon.post {
                    if (newTempList[0].weather[0].main.lowercase(Locale.getDefault()) == "clear") {
                        imageViewWeatherIcon.setBackgroundResource(R.drawable.basesunny)
                    }
                    if (newTempList[0].weather[0].main.lowercase(Locale.getDefault()) == "clouds") {
                        imageViewWeatherIcon.setBackgroundResource(R.drawable.basecloudy)
                    }
                    if (newTempList[0].weather[0].main.lowercase(Locale.getDefault()) == "rain") {
                        imageViewWeatherIcon.setBackgroundResource(R.drawable.baserainny)
                    }
                }*/

                AppInMemoryData.liveWeatherInfo!!.temperature_max = Integer.parseInt(newTempList[0].main.temp_max.toString().substring(0, 2))
                AppInMemoryData.liveWeatherInfo!!.date = newTempList[0].dt_txt
                AppInMemoryData.liveWeatherInfo!!.humidity = Integer.parseInt(newTempList[0].main.humidity.toString())

                //Sort the arrayList by ascending order of the timestamp value to get the current temperature available from the list
                newTempList.sortBy { v -> v.dt }
                tvCurrentTemperature.post {
                    tvCurrentTemperature.text = getString(
                        R.string.degree_symbol,
                        newTempList[0].main.temp.toString().substring(0, 2)
                    )
                }
                tvCurrentTempInWord.post {
                    tvCurrentTempInWord.text =
                        DVTHelperFunctions.getWeatherStatusName(newTempList[0].weather[0].main)
                }
                tvDayTempCurrent.post {
                    tvDayTempCurrent.text = getString(
                        R.string.degree_symbol,
                        newTempList[0].main.temp.toString().substring(0, 2)
                    )
                }

                todayWeatherFragment.post {
                    if (newTempList[0].weather[0].main.lowercase(Locale.getDefault()) == "clear") {
                        todayWeatherFragment.setBackgroundResource(R.drawable.forestsunny)
                        AppInMemoryData.activity!!.supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(requireActivity(), R.color.sunny_900)))
                        AppInMemoryData.activity!!.nav_menu_view.itemTextColor = ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.sunny_900))
                        AppInMemoryData.activity!!.nav_menu_view.itemIconTintList = ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.sunny_900))
                        AppInMemoryData.activity!!.nav_header_container.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.sunny_900))
                    }
                    if (newTempList[0].weather[0].main.lowercase(Locale.getDefault()) == "clouds") {
                        todayWeatherFragment.setBackgroundResource(R.drawable.forestcloudy)
                        AppInMemoryData.activity!!.supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(requireActivity(), R.color.cloudy_900)))
                        AppInMemoryData.activity!!.nav_menu_view.itemTextColor = ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.cloudy_900))
                        AppInMemoryData.activity!!.nav_menu_view.itemIconTintList = ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.cloudy_900))
                        AppInMemoryData.activity!!.nav_header_container.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.cloudy_900))
                    }
                    if (newTempList[0].weather[0].main.lowercase(Locale.getDefault()) == "rain") {
                        todayWeatherFragment.setBackgroundResource(R.drawable.forestrainy)
                        AppInMemoryData.activity!!.supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(requireActivity(), R.color.rainy_900)))
                        AppInMemoryData.activity!!.nav_menu_view.itemTextColor = ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.rainy_900))
                        AppInMemoryData.activity!!.nav_menu_view.itemIconTintList = ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.rainy_900))
                        AppInMemoryData.activity!!.nav_header_container.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.rainy_900))
                    }
                }

                AppInMemoryData.liveWeatherInfo!!.description = DVTHelperFunctions.getWeatherStatusName(newTempList[0].weather[0].main)
                AppInMemoryData.liveWeatherInfo!!.temperature = Integer.parseInt(newTempList[0].main.temp.toString().substring(0, 2))
                AppInMemoryData.liveWeatherInfo!!.temperature_feel = Integer.parseInt(newTempList[0].main.feels_like.toString().substring(0, 2))
            }
        }
        catch(ex:Exception){
            Log.d("TodayTempReadings", "Internal error reading today temperature: ${Gson().toJson(ex)}")
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun extractFiveDaysTemperatureReadings(tempList:ArrayList<ListWeatherModel>, day:Int){
        try{
            if(tempList.isNullOrEmpty())
                return

            tempList.sortBy{it -> it.dt_txt}
            val calender = Calendar.getInstance()
            calender.time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(tempList[0].dt_txt)!!
            calender.add(Calendar.DAY_OF_YEAR, day)
            var newTempList = arrayListOf<ListWeatherModel>()
            tempList.forEach { temp ->
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(temp.dt_txt)
                val apiResponseDate = format.format(date!!)
                val todayDate = format.format(calender.time)
                if(todayDate.equals(apiResponseDate)){
                    newTempList.add(temp)
                }
            }


            newTempList.sortByDescending { v -> v.main.temp_max }
            val extendedWeather = LiveExtendedWeatherInformation()
            extendedWeather.temperature = Integer.parseInt(newTempList[0].main.temp_max.toString().substring(0, 2))
            extendedWeather.temperature_feel = Integer.parseInt(newTempList[0].main.feels_like.toString().substring(0, 2))
            extendedWeather.temperature_min = Integer.parseInt(newTempList[0].main.temp_min.toString().substring(0, 2))
            extendedWeather.temperature_max = Integer.parseInt(newTempList[0].main.temp_max.toString().substring(0, 2))
            extendedWeather.humidity = Integer.parseInt(newTempList[0].main.humidity.toString())
            extendedWeather.description = DVTHelperFunctions.getWeatherStatusName(newTempList[0].weather[0].main)
            extendedWeather.dayOfWeek = SimpleDateFormat("EEEE").format(calender.time)
            extendedWeather.date = newTempList[0].dt_txt
            extendedWeather.position = day

            if(AppInMemoryData.liveExtendedWeatherData == null)
                AppInMemoryData.liveExtendedWeatherData = arrayListOf()
            AppInMemoryData.liveExtendedWeatherData!!.add(extendedWeather)

            if(newTempList.isNotEmpty()) {
                when (day) {
                    1 -> {
                        tvDayOneWeekDay.post {
                            tvDayOneWeekDay.text = SimpleDateFormat("EEEE").format(calender.time)
                        }
                        tvDayOneTempValue.post {
                            tvDayOneTempValue.text = getString(
                                R.string.degree_symbol,
                                newTempList[0].main.temp_max.toString().substring(0, 2)
                            )
                        }
                        imgDayOneTempIcon.post {
                            if (newTempList[0].weather[0].main.lowercase(Locale.getDefault()) == "clear") {
                                imgDayOneTempIcon.setBackgroundResource(R.drawable.clear)
                            }
                            if (newTempList[0].weather[0].main.lowercase(Locale.getDefault()) == "clouds") {
                                imgDayOneTempIcon.setBackgroundResource(R.drawable.partlysunny)
                            }
                            if (newTempList[0].weather[0].main.lowercase(Locale.getDefault()) == "rain") {
                                imgDayOneTempIcon.setBackgroundResource(R.drawable.rain)
                            }
                        }
                    }

                    2 -> {
                        tvDayTwoWeekDay.post {
                            tvDayTwoWeekDay.text =
                                SimpleDateFormat("EEEE").format(calender.time)
                        }
                        tvDayTwoTempValue.post {
                            tvDayTwoTempValue.text = getString(
                                R.string.degree_symbol,
                                newTempList[0].main.temp_max.toString().substring(0, 2)
                            )
                        }
                        imgDayTwoTempIcon.post {
                            if (newTempList[0].weather[0].main.lowercase(Locale.getDefault()) == "clear") {
                                imgDayTwoTempIcon.setBackgroundResource(R.drawable.clear)
                            }
                            if (newTempList[0].weather[0].main.lowercase(Locale.getDefault()) == "clouds") {
                                imgDayTwoTempIcon.setBackgroundResource(R.drawable.partlysunny)
                            }
                            if (newTempList[0].weather[0].main.lowercase(Locale.getDefault()) == "rain") {
                                imgDayTwoTempIcon.setBackgroundResource(R.drawable.rain)
                            }
                        }
                    }

                    3 -> {
                        tvDayThreeWeekDay.post {
                            tvDayThreeWeekDay.text =
                                SimpleDateFormat("EEEE").format(calender.time)
                        }
                        tvDayThreeTempValue.post {
                            tvDayThreeTempValue.text = getString(
                                R.string.degree_symbol,
                                newTempList[0].main.temp_max.toString().substring(0, 2)
                            )
                        }
                        imgDayThreeTempIcon.post {
                            if (newTempList[0].weather[0].main.lowercase(Locale.getDefault()) == "clear") {
                                imgDayThreeTempIcon.setBackgroundResource(R.drawable.clear)
                            }
                            if (newTempList[0].weather[0].main.lowercase(Locale.getDefault()) == "clouds") {
                                imgDayThreeTempIcon.setBackgroundResource(R.drawable.partlysunny)
                            }
                            if (newTempList[0].weather[0].main.lowercase(Locale.getDefault()) == "rain") {
                                imgDayThreeTempIcon.setBackgroundResource(R.drawable.rain)
                            }
                        }
                    }

                    4 -> {
                        tvDayFourWeekDay.post {
                            tvDayFourWeekDay.text =
                                SimpleDateFormat("EEEE").format(calender.time)
                        }
                        tvDayFourTempValue.post {
                            tvDayFourTempValue.text = getString(
                                R.string.degree_symbol,
                                newTempList[0].main.temp_max.toString().substring(0, 2)
                            )
                        }
                        imgDayFourTempIcon.post {
                            if (newTempList[0].weather[0].main.lowercase(Locale.getDefault()) == "clear") {
                                imgDayFourTempIcon.setBackgroundResource(R.drawable.clear)
                            }
                            if (newTempList[0].weather[0].main.lowercase(Locale.getDefault()) == "clouds") {
                                imgDayFourTempIcon.setBackgroundResource(R.drawable.partlysunny)
                            }
                            if (newTempList[0].weather[0].main.lowercase(Locale.getDefault()) == "rain") {
                                imgDayFourTempIcon.setBackgroundResource(R.drawable.rain)
                            }
                        }
                    }

                    5 -> {
                        tvDayFiveWeekDay.post {
                            tvDayFiveWeekDay.text =
                                SimpleDateFormat("EEEE").format(calender.time)
                        }
                        tvDayFiveTempValue.post {
                            tvDayFiveTempValue.text = getString(
                                R.string.degree_symbol,
                                newTempList[0].main.temp_max.toString().substring(0, 2)
                            )
                        }
                        imgDayFiveTempIcon.post {
                            if (newTempList[0].weather[0].main.lowercase(Locale.getDefault()) == "clear") {
                                imgDayFiveTempIcon.setBackgroundResource(R.drawable.clear)
                            }
                            if (newTempList[0].weather[0].main.lowercase(Locale.getDefault()) == "clouds") {
                                imgDayFiveTempIcon.setBackgroundResource(R.drawable.partlysunny)
                            }
                            if (newTempList[0].weather[0].main.lowercase(Locale.getDefault()) == "rain") {
                                imgDayFiveTempIcon.setBackgroundResource(R.drawable.rain)
                            }
                        }
                    }
                }
            }
        }
        catch(ex:Exception){
            Log.d("TodayTempReadings", "Internal error reading today temperature: ${Gson().toJson(ex)}")
        }
    }


    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.requireActivity().onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            AppBasicConstants.PERMISSION_REQUEST_CODE ->{
                if(canAccessLocation() && canAccessCoreLocation()){
                    initLocationService()
                }
            }
        }
    }
}