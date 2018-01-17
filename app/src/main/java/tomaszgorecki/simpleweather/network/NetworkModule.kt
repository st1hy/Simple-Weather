package tomaszgorecki.simpleweather.network

import com.google.gson.*
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.createWithScheduler
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier annotation class AccuWeather
@Qualifier annotation class OpenWeather

@Module abstract class NetworkModule {

    @Module companion object {

        @Singleton
        @Provides
        @JvmStatic
        fun gson(): Gson {
            return GsonBuilder()
                    .registerTypeAdapter(Date::class.java, JsonSerializer<Date> {
                        src, _, _ -> JsonPrimitive(src.time)
                    })
                    .registerTypeAdapter(Date::class.java, JsonDeserializer<Date> {
                        jsonElement, _, _ -> Date(jsonElement.asJsonPrimitive.asLong)
                    })
                    .create()
        }


        @Singleton
        @Provides
        @AccuWeather
        @JvmStatic
        fun accuWeather(gson: Gson): Retrofit {
            return Retrofit.Builder().baseUrl(AccuweatherService.BASE_URI)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(createWithScheduler(Schedulers.io()))
                    .build()
        }
        @Singleton
        @Provides
        @OpenWeather
        @JvmStatic
        fun openWeather(gson: Gson): Retrofit {
            return Retrofit.Builder().baseUrl(OpenWeatherMapService.BASE_URI)
                    .client(OkHttpClient.Builder()
                            .addInterceptor(HttpLoggingInterceptor()
                                    .setLevel(HttpLoggingInterceptor.Level.BODY))
                            .build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(createWithScheduler(Schedulers.io()))
                    .build()
        }


        @Singleton
        @Provides
        @JvmStatic
        fun accuweatherService(@AccuWeather retrofit: Retrofit): AccuweatherService {
            return retrofit.create(AccuweatherService::class.java)
        }

        @Singleton
        @Provides
        @JvmStatic
        fun openWeatherService(@OpenWeather retrofit: Retrofit): OpenWeatherMapService {
            return retrofit.create(OpenWeatherMapService::class.java)
        }
    }

}