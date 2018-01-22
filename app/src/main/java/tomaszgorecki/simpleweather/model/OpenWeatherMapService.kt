package tomaszgorecki.simpleweather.model

import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface OpenWeatherMapService {

    @GET("find")
    fun find(
            @Query("appid") apiId: String = API_KEY,
            @Query("q") query: String,
            @Query("type") type: Type = Type.LIKE,
            @Query("units") units: Units = Units.METRIC,
            @Query("lang") lang: String = Locale.getDefault().language
    ): Single<OpenWeatherFindResult>


    @GET("weather")
    fun weather(
            @Query("appid") apiId: String = API_KEY,
            @Query("id") cityId: Long?,
            @Query("units") units: Units = Units.METRIC,
            @Query("lang") lang: String = Locale.getDefault().language
    ): Single<OpenWeatherCity>

    companion object {
        const val API_KEY = "577648702833ddcd32cca67396304083"
        const val BASE_URI = "http://api.openweathermap.org/data/2.5/"
        const val ICON_BASE_URI = "http://openweathermap.org/img/w/"
    }

    object Builder {

        fun openWeather(gson: Gson): Retrofit {
            return Retrofit.Builder().baseUrl(OpenWeatherMapService.BASE_URI)
                    .client(OkHttpClient.Builder()
                            .addInterceptor(HttpLoggingInterceptor()
                                    .setLevel(HttpLoggingInterceptor.Level.BODY))
                            .build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory
                            .createWithScheduler(Schedulers.io()))
                    .build()
        }
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
