package co.sz.vusieam.mobileweathertest.services.data

//import com.google.common.truth.Truth.assertThat
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import co.sz.vusieam.mobileweathertest.getOrAwaitValue
import co.sz.vusieam.mobileweathertest.models.entities.CityEntity
import co.sz.vusieam.mobileweathertest.models.entities.ExtendedWeatherEntity
import co.sz.vusieam.mobileweathertest.models.entities.WeatherEntity
import co.sz.vusieam.mobileweathertest.utils.DVTHelperFunctions
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class WeatherDatabaseServiceTest : TestCase(){

    private lateinit var db: WeatherDatabaseService
    private lateinit var dao:WeatherServiceDao

    @Before
    public override fun setUp(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, WeatherDatabaseService::class.java).build()
        dao = db.getWeatherServiceDao()
    }


    @After
    @Throws(IOException::class)
    fun closeDb(){
        db.close()
    }

    @Test
    fun testInsertWeather() //= runBlocking
    {
        val strID = DVTHelperFunctions.generateUID()
        val weather = WeatherEntity(strID, 21, 18, 18, 26, 32, "Cloudy", "2021-11-14 13:27:44")
        db.getWeatherServiceDao().insertWeather(weather)

        val city = CityEntity(strID, 981084, "Lyttelton", "-25.8375", "28.196", "ZA")
        db.getWeatherServiceDao().insertCity(city)

        val extendedWeather1 = ExtendedWeatherEntity(strID,21, 18, 18, 26, 32, "Cloudy", "Monday","2021-11-15 13:27:44", 0)
        val extendedWeather2 = ExtendedWeatherEntity(strID,21, 18, 18, 26, 32, "Sunny", "Tuesday","2021-11-16 13:27:44", 0)
        val extendedWeather3 = ExtendedWeatherEntity(strID,21, 18, 18, 26, 32, "Rainy", "Wednesday","2021-17-14 13:27:44", 0)
        val extendedWeather4 = ExtendedWeatherEntity(strID,21, 18, 18, 26, 32, "Cloudy", "Thursday","2021-18-14 13:27:44", 0)
        val extendedWeather5 = ExtendedWeatherEntity(strID,21, 18, 18, 26, 32, "Rainy", "Friday","2021-11-19 13:27:44", 0)

        val allWeather = arrayListOf<ExtendedWeatherEntity>(extendedWeather1, extendedWeather2, extendedWeather3, extendedWeather4, extendedWeather5)
        db.getWeatherServiceDao().insertBulkExtendedWeather(allWeather)

        val dbWeather= db.getWeatherServiceDao().getAllSavedWeather()
        val result = dbWeather.getOrAwaitValue().contains(weather)
        assertThat(result).isTrue()
    }


}

