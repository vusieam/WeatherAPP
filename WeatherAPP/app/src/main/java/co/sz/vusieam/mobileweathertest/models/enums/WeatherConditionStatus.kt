package co.sz.vusieam.mobileweathertest.models.enums

enum class WeatherConditionStatus(val status:String) {
    SUNNY("Clear"),
    RAINY("Rain"),
    CLOUDY("Clouds");

    companion object {
        private val types = values().associateBy { it.status }
        fun findByValue(value: String) = types[value]
    }
}


enum class WeatherConditionIntStatus(val status:Int) {
    ANY(0),
    SUNNY(1),
    RAINY(2),
    CLOUDY(3);

    companion object {
        private val types = values().associateBy { it.status }
        fun findByValue(value: Int) = types[value]
    }
}