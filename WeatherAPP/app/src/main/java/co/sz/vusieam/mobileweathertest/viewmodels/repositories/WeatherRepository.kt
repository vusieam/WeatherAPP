package co.sz.vusieam.mobileweathertest.viewmodels.repositories

import androidx.lifecycle.LiveData
import co.sz.vusieam.mobileweathertest.models.entities.*
import co.sz.vusieam.mobileweathertest.models.linkedentities.WeatherAndCityLinked
import co.sz.vusieam.mobileweathertest.models.linkedentities.WeatherAndExtendedWeather
import co.sz.vusieam.mobileweathertest.services.data.WeatherServiceDao

class WeatherRepository(private val weatherServiceDao : WeatherServiceDao) {

    //val allSavedWeather : LiveData<List<WeatherEntity>> = weatherServiceDao.getAllSavedWeather()
    val allSavedWeatherAndCity : LiveData<List<WeatherAndCityLinked>> = weatherServiceDao.getCityAndWeather()

    //region Favourite weather information calls

    suspend fun insertWeather(weather: WeatherEntity) {
        weatherServiceDao.insertWeather(weather)
    }

    fun getLastWeatherID():Int {
        return weatherServiceDao.getLastWeatherID()
    }

    suspend fun insertCity(city: CityEntity){
        weatherServiceDao.insertCity(city)
    }

    suspend fun insertExtendedWeather(extendedWeather: ExtendedWeatherEntity) {
        weatherServiceDao.insertExtendedWeather(extendedWeather)
    }

    suspend fun insertBulkExtendedWeather(bulkExtendedWeather: List<ExtendedWeatherEntity>) {
        weatherServiceDao.insertBulkExtendedWeather(bulkExtendedWeather)
    }

    suspend fun getCityByWeatherID(id:Int) : LiveData<WeatherAndCityLinked>{
        return weatherServiceDao.getCityByWeatherID(id)
    }

    suspend fun getCityAndWeather() : LiveData<List<WeatherAndCityLinked>>{
        return weatherServiceDao.getCityAndWeather()
    }

    suspend fun getExtendedWeatherByWeatherID(id:Int) : LiveData<List<WeatherAndExtendedWeather>>{
        return weatherServiceDao.getExtendedWeatherByWeatherID(id)
    }
    //endregion



    /*suspend fun insertFavWeatherBase(baseWeather: FavWeatherBase) {
        weatherServiceDao.insertFavWeatherBase(baseWeather)
    }

    suspend fun insertFavWeather(weather: FavWeather) {
        weatherServiceDao.insertFavWeather(weather)
    }

//    suspend fun getLastInternalBaseID():Int {
//        return weatherServiceDao.getLastInternalBaseID()
//    }

    suspend fun insertFavWeatherClouds(cloud: FavWeatherClouds) {
        weatherServiceDao.insertFavWeatherClouds(cloud)
    }

    suspend fun insertFavWeatherWind(wind : FavWeatherWind) {
        weatherServiceDao.insertFavWeatherWind(wind)
    }

    suspend fun insertFavWeatherCoordinates(coord : FavWeatherCoordinates) {
        weatherServiceDao.insertFavWeatherCoordinates(coord)
    }

    suspend fun insertFavWeatherMain(main : FavWeatherMain) {
        weatherServiceDao.insertFavWeatherMain(main)
    }*/
}