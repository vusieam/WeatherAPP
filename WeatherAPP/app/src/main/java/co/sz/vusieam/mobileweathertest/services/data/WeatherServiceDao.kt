package co.sz.vusieam.mobileweathertest.services.data

import androidx.lifecycle.LiveData
import androidx.room.*
import co.sz.vusieam.mobileweathertest.models.entities.*
import co.sz.vusieam.mobileweathertest.models.linkedentities.WeatherAndCityLinked
import co.sz.vusieam.mobileweathertest.models.linkedentities.WeatherAndExtendedWeather

@Dao
interface WeatherServiceDao {

    //region New Dao Function Calls

    /**
     * Insert new favourite weather information into the database
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWeather(weather: WeatherEntity)

    /**
     * Retrieve the last weather id from the weather information table in the database
     */
    @Query("Select count(*) from TblWeather")
    fun getLastWeatherID(): Int

    /**
     * Insert new favourite city to the database
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insertCity(city: CityEntity)

    /**
     * Insert 5 day favourite weather information to the database
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExtendedWeather(extendedWeather: ExtendedWeatherEntity)


    /**
     * Insert bulk 5 day favourite weather information to the database
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBulkExtendedWeather(extendedWeather: List<ExtendedWeatherEntity>)


    /**
     * Retrieve the city linked to a specific weather from the database
     */
    @Transaction
    @Query("Select * from TblWeather")
    fun getAllSavedWeather() : LiveData<List<WeatherEntity>>

    /**
     * Retrieve the city linked to a specific weather from the database
     */
    /*@Transaction
    @Query("Select * from TblWeather")
    fun getCityAndWeather() : LiveData<List<WeatherAndCityLinked>>*/

    /**
     * Retrieve the city linked to a specific weather from the database
     */
    @Transaction
    @Query("SELECT * FROM TblWeather W inner join TblCities C on C.c_weatherID = W.w_internalID")
    fun getCityAndWeather() : LiveData<List<WeatherAndCityLinked>>

    /**
     * Retrieve the city linked to a specific weather from the database
     */
    @Transaction
    @Query("Select * from TblWeather where w_internalID = :id limit 1")
    fun getCityByWeatherID(id:Int) : LiveData<WeatherAndCityLinked>

    /**
     * Retrieve the 5 days extended weathers linked to a specific weather from the database
     */
    @Transaction
    @Query("Select * from TblWeather where w_internalID = :id")
    fun getExtendedWeatherByWeatherID(id:Int) : LiveData<List<WeatherAndExtendedWeather>>

    //endregion


    //region Insert functions
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insertFavWeatherBase(baseWeather: FavWeatherBase)
//
//
//
////    @Query("Select InternalBaseID from TblFavWeatherBase order by InternalBaseID desc limit 1")
////    suspend fun getLastInternalBaseID(): String?
//
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insertFavWeather(weather: FavWeather)
//
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insertFavWeatherClouds(clouds: FavWeatherClouds)
//
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insertFavWeatherWind(wind: FavWeatherWind)
//
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insertFavWeatherCoordinates(co_ord: FavWeatherCoordinates)
//
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insertFavWeatherMain(main: FavWeatherMain)
//    //endregion
//
//    //region Delete functions
//    @Delete
//    suspend fun delete(baseWeather: FavWeatherBase)
//
//    //endregion
//
//    //region Query functions
//    @Transaction
//    @Query("Select * from TblFavWeatherBase order by InternalBaseID asc")
//    fun getAllSavedFavWeather() : LiveData<List<FavWeatherLinked>>

    //endregion
}