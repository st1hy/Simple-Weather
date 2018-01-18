package tomaszgorecki.simpleweather.model

import android.graphics.Color
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.google.gson.annotations.SerializedName
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter
import paperparcel.PaperParcel
import paperparcel.PaperParcelable
import timber.log.Timber
import tomaszgorecki.simpleweather.app.WeatherApp
import tomaszgorecki.simpleweather.network.OpenWeatherMapService
import java.util.*

@PaperParcel
data class OpenWeatherFindResult(
        @SerializedName("list") val cities: List<OpenWeatherCity>?,
        val message: String?,
        val count: Int?
) : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelOpenWeatherFindResult.CREATOR
    }
}

@Entity
@PaperParcel
data class OpenWeatherCityEntity(
        @Id var id: Long,
        val cityId: Long,
        var name: String?,
        var lastUsed: Date,
        @Convert(converter = JsonConverter::class, dbType = String::class)
        var city: OpenWeatherCity
) : PaperParcelable {

    constructor(city: OpenWeatherCity) : this(0, city.id, city.name, Date(), city)

    companion object {
        @JvmField val CREATOR = PaperParcelOpenWeatherCityEntity.CREATOR
    }

    fun update(newCity: OpenWeatherCity): OpenWeatherCityEntity {
        if (cityId != newCity.id) {
            Timber.e("City id changed (!) old: $cityId , new ${newCity.id}")
        } else {
            lastUsed = Date()
            city = newCity
            name = newCity.name
        }
        return this
    }
}

@PaperParcel
data class OpenWeatherCity(
        val id: Long,
        val name: String?,
        val main: OpenWeatherData?,
        @SerializedName("dt") val timestamp: Date?,
        val wind: OpenWeatherWind?,
        val sys: OpenWeatherSys?,
        val snow: OpenWeatherSnow?,
        val rain: OpenWeatherRain?,
        val clouds: OpenWeatherClouds?,
        val weather: List<OpenWeatherInner>?,
        val visibility: Int?,
        val cod: Int?,
        val base: String?
) : PaperParcelable, SearchSuggestion {

    companion object {
        @JvmField val CREATOR = PaperParcelOpenWeatherCity.CREATOR
    }

    override fun getBody(): String {
        return "$name, ${sys?.country}"
    }

    fun toEntity(): OpenWeatherCityEntity {
        return OpenWeatherCityEntity(this)
    }

    fun hasTemperature(): Boolean = main?.temp != null
    fun hasMinTemperature(): Boolean = main?.temp_min != null
    fun hasMaxTemperature(): Boolean = main?.temp_max != null

    fun getTemperature(): String = tempStringOf(main?.temp)
    fun getMinTemperature(): String = tempStringOf(main?.temp_min)
    fun getMaxTemperature(): String = tempStringOf(main?.temp_max)
    fun getMinTemp(): Float? = main?.temp_min
    fun getMaxTemp(): Float? = main?.temp_max
    private fun tempStringOf(temp: Float?): String = if (temp != null) "$temp °C" else ""

    fun tempColor(): Int = tempColor(main?.temp)
    fun tempColor(temp: Float?): Int {
        return when {
            temp == null -> Color.BLACK
            temp < 10 -> Color.BLUE
            temp > 20 -> Color.RED
            else -> Color.BLACK
        }
    }

    fun hasWeatherIcon(): Boolean = weather?.firstOrNull()?.icon != null
    fun getWeatherName(): String = weather?.firstOrNull()?.description ?: ""
    fun getWeatherIcon(): String? = weather?.firstOrNull()?.icon?.let {
        OpenWeatherMapService.ICON_BASE_URI + it + ".png"
    }
}

@PaperParcel
data class OpenWeatherData(
        val temp: Float?,
        val pressure: Float?,
        val humidity: Float?,
        val temp_min: Float?,
        val temp_max: Float?
) : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelOpenWeatherData.CREATOR
    }
}

@PaperParcel
data class OpenWeatherWind(
        val speed: Float?,
        val deg: Float?
)  : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelOpenWeatherWind.CREATOR
    }
}

@PaperParcel
data class OpenWeatherSys(
        val country: String?,
        val type: Int?,
        val id: Int?,
        val sunrise: Date?,
        val sunset: Date?,
        val message: Float?
) : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelOpenWeatherSys.CREATOR
    }
}

@PaperParcel
data class OpenWeatherRain(
        @SerializedName("3h") val treeHours: Float?
) : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelOpenWeatherRain.CREATOR
    }
}

@PaperParcel
data class OpenWeatherSnow(
        @SerializedName("3h") val treeHours: Float?
)  : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelOpenWeatherSnow.CREATOR
    }
}

@PaperParcel
data class OpenWeatherClouds(
        val all: Int?
)  : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelOpenWeatherClouds.CREATOR
    }
}

@PaperParcel
data class OpenWeatherInner(
        val id: Int?,
        @SerializedName("main")
        val name: String?,
        val description: String?,
        val icon: String?
) : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelOpenWeatherInner.CREATOR
    }
}

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