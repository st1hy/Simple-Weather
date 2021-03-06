package tomaszgorecki.simpleweather.model

import android.databinding.BaseObservable
import android.graphics.Color
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.google.gson.annotations.SerializedName
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Uid
import paperparcel.PaperParcel
import paperparcel.PaperParcelable
import timber.log.Timber
import tomaszgorecki.simpleweather.utils.JsonConverter
import java.util.*

@Uid(9083887423634301212)
@Entity @PaperParcel data class CityEntity(
        @Id var id: Long,
        val cityId: Long,
        var name: String?,
        var lastUsed: Date,
        var lastUpdated: Date?,
        @Convert(converter = JsonConverter::class, dbType = String::class)
        var city: City
) : PaperParcelable {

    constructor(city: City) : this(0, city.id, city.name, Date(), Date(), city)

    companion object {
        @JvmField val CREATOR = PaperParcelCityEntity.CREATOR
    }

    fun update(newCity: City): CityEntity {
        if (cityId != newCity.id) {
            Timber.e("City id changed (!) old: $cityId , new ${newCity.id}")
        } else {
            lastUsed = Date()
            lastUpdated = lastUsed
            city = newCity
            name = newCity.name
            city.notifyChange()
        }
        return this
    }
}

@PaperParcel data class SearchResult(
        @SerializedName("list") val cities: List<City>?,
        val message: String?,
        val count: Int?
) : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelSearchResult.CREATOR
    }
}

@PaperParcel data class City(
        val id: Long,
        val name: String?,
        val main: Data?,
        @SerializedName("dt") val timestamp: Date?,
        val wind: Wind?,
        val sys: Sys?,
        val snow: Precipitation?,
        val rain: Precipitation?,
        val clouds: Clouds?,
        val weather: List<Weather>?,
        val visibility: Int?,
        val cod: Int?,
        val base: String?
) : PaperParcelable, SearchSuggestion, BaseObservable() {

    companion object {
        @JvmField val CREATOR = PaperParcelCity.CREATOR
    }

    override fun getBody() = "$name, ${sys?.country}"

    fun toEntity() = CityEntity(this)

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

    @PaperParcel data class Data(
            val temp: Float?,
            val pressure: Float?,
            val humidity: Float?,
            val temp_min: Float?,
            val temp_max: Float?
    ) : PaperParcelable {
        companion object {
            @JvmField val CREATOR = PaperParcelCity_Data.CREATOR
        }
    }

    @PaperParcel data class Wind(
            val speed: Float?,
            val deg: Float?
    ) : PaperParcelable {
        companion object {
            @JvmField val CREATOR = PaperParcelCity_Wind.CREATOR
        }
    }

    @PaperParcel data class Sys(
            val country: String?,
            val type: Int?,
            val id: Int?,
            val sunrise: Date?,
            val sunset: Date?,
            val message: Float?
    ) : PaperParcelable {
        companion object {
            @JvmField val CREATOR = PaperParcelCity_Sys.CREATOR
        }
    }

    @PaperParcel data class Precipitation(
            @SerializedName("3h") val treeHours: Float?
    ) : PaperParcelable {
        companion object {
            @JvmField val CREATOR = PaperParcelCity_Precipitation.CREATOR
        }
    }

    @PaperParcel data class Clouds(
            val all: Int?
    ) : PaperParcelable {
        companion object {
            @JvmField val CREATOR = PaperParcelCity_Clouds.CREATOR
        }
    }

    @PaperParcel data class Weather(
            val id: Int?,
            @SerializedName("main")
            val name: String?,
            val description: String?,
            val icon: String?
    ) : PaperParcelable {
        companion object {
            @JvmField val CREATOR = PaperParcelCity_Weather.CREATOR
        }
    }

}
