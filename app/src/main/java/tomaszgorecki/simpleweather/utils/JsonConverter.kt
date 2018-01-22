package tomaszgorecki.simpleweather.utils

import io.objectbox.converter.PropertyConverter
import tomaszgorecki.simpleweather.WeatherApp
import tomaszgorecki.simpleweather.model.OpenWeatherCity

class JsonConverter : PropertyConverter<OpenWeatherCity, String> {
    override fun convertToDatabaseValue(entityProperty: OpenWeatherCity?): String? {
        if (entityProperty == null) return null
        return WeatherApp.gson.value.toJson(entityProperty, OpenWeatherCity::class.java)
    }

    override fun convertToEntityProperty(databaseValue: String?): OpenWeatherCity? {
        if (databaseValue == null) return null
        return WeatherApp.gson.value.fromJson(databaseValue, OpenWeatherCity::class.java)
    }
}