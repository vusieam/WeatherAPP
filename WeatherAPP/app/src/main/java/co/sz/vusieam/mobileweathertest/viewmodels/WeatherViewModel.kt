package co.sz.vusieam.mobileweathertest.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import co.sz.vusieam.mobileweathertest.models.entities.*
import co.sz.vusieam.mobileweathertest.models.linkedentities.WeatherAndCityLinked
import co.sz.vusieam.mobileweathertest.services.data.WeatherDatabaseService
import co.sz.vusieam.mobileweathertest.viewmodels.repositories.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel(application : Application) : AndroidViewModel(application) {

    val allWeatherAndCity : LiveData<List<WeatherAndCityLinked>>
    private val repository : WeatherRepository

    init {
        val dao = WeatherDatabaseService.getDatabaseInstance(application).getWeatherServiceDao()
        repository = WeatherRepository(dao)
        allWeatherAndCity = repository.allSavedWeatherAndCity
    }

    fun insertWeather(weather: WeatherEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertWeather(weather)
    }

    fun insertCity(city: CityEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertCity(city)
    }

    fun insertExtendedWeather(extendedWeather: ExtendedWeatherEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertExtendedWeather(extendedWeather)
    }


    fun insertBulkExtendedWeather(extendedWeather: List<ExtendedWeatherEntity>) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertBulkExtendedWeather(extendedWeather)
    }

}