package co.sz.vusieam.mobileweathertest.utils


object ApiEndpointConstants {
    /**
     * API Key
     */
    private const val apiKey = "fd0b857258730e3c99cb11c817c373f6"

    private fun getBaseEndpointURL():String{
        return "https://api.openweathermap.org/data/2.5/"
    }


    fun getWeatherByGeoCoordinates(latitude:String, longitude:String):String{
        return "${getBaseEndpointURL()}forecast?lat=$latitude&lon=$longitude&appid=$apiKey&units=metric"
    }

    fun getWeatherByCity(city:String):String{
        return "${getBaseEndpointURL()}forecast?q=$city&appid=$apiKey&units=metric"
    }

    override fun toString(): String {
        return getBaseEndpointURL()
    }

}