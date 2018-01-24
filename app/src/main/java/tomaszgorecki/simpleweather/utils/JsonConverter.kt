package tomaszgorecki.simpleweather.utils

import io.objectbox.converter.PropertyConverter
import tomaszgorecki.simpleweather.WeatherApp
import tomaszgorecki.simpleweather.model.City

class JsonConverter : PropertyConverter<City, String> {
    override fun convertToDatabaseValue(entityProperty: City?): String? {
        if (entityProperty == null) return null
        return WeatherApp.gson.value.toJson(entityProperty, City::class.java)
    }

    override fun convertToEntityProperty(databaseValue: String?): City? {
        if (databaseValue == null) return null
        return WeatherApp.gson.value.fromJson(databaseValue, City::class.java)
    }
}