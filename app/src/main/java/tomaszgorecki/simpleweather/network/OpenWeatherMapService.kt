package tomaszgorecki.simpleweather.network

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.google.gson.annotations.SerializedName
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter
import io.reactivex.Single
import paperparcel.PaperParcel
import paperparcel.PaperParcelable
import retrofit2.http.GET
import retrofit2.http.Query
import tomaszgorecki.simpleweather.app.WeatherApp
import java.util.*

interface OpenWeatherMapService {

    @GET("find")
    fun find(
            @Query("appid") apiId: String = API_KEY,
            @Query("q") query: String,
            @Query("type") type: Type = Type.LIKE,
            @Query("units") units: Units = Units.METRIC,
            @Query("lang") lang: String = Locale.getDefault().language
    ) : Single<OpenWeatherFindResult>

    companion object {
        val API_KEY = "577648702833ddcd32cca67396304083"
        val BASE_URI = "http://api.openweathermap.org/data/2.5/"
    }
}

enum class Units {
    METRIC, IMPERIAL, DEFAULT
}

/** Search precision **/
enum class Type {
    ACCURATE,
    LIKE //approximation
}

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
        val name: String?,
        var lastUsed: Date,
        @Convert(converter = JsonConverter::class, dbType = String::class)
        val city: OpenWeatherCity) : PaperParcelable {

    constructor(city: OpenWeatherCity) : this(0, city.id, city.name, Date(), city)

    companion object {
        @JvmField val CREATOR = PaperParcelOpenWeatherCityEntity.CREATOR
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
        val weather: List<OpenWeatherInner>?
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
        val country: String?
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
        val main: String?,
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