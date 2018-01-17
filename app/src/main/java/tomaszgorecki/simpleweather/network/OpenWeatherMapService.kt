package tomaszgorecki.simpleweather.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import tomaszgorecki.simpleweather.model.OpenWeatherFindResult
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
        const val API_KEY = "577648702833ddcd32cca67396304083"
        const val BASE_URI = "http://api.openweathermap.org/data/2.5/"
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
