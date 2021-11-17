package co.sz.vusieam.mobileweathertest.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

object DVTHelperFunctions {

    fun getWeatherStatusName(status:String): String {
        when (status.lowercase(Locale.getDefault())) {
            "clouds" -> return "Cloudy"
            "rain" -> return "Rainy"
            "clear" -> return "Sunny"
            else -> return "Sunny"
        }
    }

    fun generateUID(): String {
        val format = SimpleDateFormat("yyMMddHHmmssSSS", Locale.getDefault())
        return format.format(Date())

    }
}


fun<T> LiveData<T>.getOrAwaitValue() : T {

    var data: T? = null
    val latch = CountDownLatch( 1)

    val observer = object: Observer<T> {

        override fun onChanged(t: T) {
            data = t
            this@getOrAwaitValue.removeObserver(this)
            latch.countDown()
        }
    }

    this.observeForever(observer)
    try {
        if (latch.await(2, TimeUnit.SECONDS))
            throw TimeoutException("Live Data never got it's value")
    }
    finally {
        this.removeObserver(observer)
    }
    return data as T
}