package tomaszgorecki.simpleweather.inject

import android.app.Activity
import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import com.google.gson.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.objectbox.Box
import io.objectbox.BoxStore
import kotlinx.android.synthetic.main.city_list.*
import retrofit2.Retrofit
import tomaszgorecki.simpleweather.WeatherApp
import tomaszgorecki.simpleweather.model.CityEntity
import tomaszgorecki.simpleweather.model.MyObjectBox
import tomaszgorecki.simpleweather.model.OpenWeatherMapService
import java.util.*
import javax.inject.Singleton

@Module(includes = arrayOf(NetworkModule::class, DatabaseModule::class))
abstract class AppModule {

    @Binds @AppContext @PerApp abstract fun appContext(app: WeatherApp): Context
}

@Module class ActivityModule(private var activity: AppCompatActivity) {

    @Provides @ActivityContext fun activityContext(): Context = activity

    @Provides fun activity(): Activity = activity

    @Provides fun appCompatActivity(): AppCompatActivity = activity

    @Provides fun supportFragmentManager(): FragmentManager = activity.supportFragmentManager

}

@Module abstract class CityListActivityModule {

    @Module companion object {
        @Provides @JvmStatic @PerActivity fun twoPanel(activity: Activity): PanelMode {
            return if (activity.city_detail_container != null) PanelMode.TWO_PANE
            else PanelMode.SINGLE
        }
    }

    @Binds abstract fun listItemComponentBuilder(component: CityListActivityComponent)
            : CityListItemComponentBuilder

}

enum class PanelMode {
    SINGLE, TWO_PANE
}

@Module object NetworkModule {

    @Singleton @Provides @OpenWeather @JvmStatic fun openWeather(gson: Gson): Retrofit =
            OpenWeatherMapService.Builder.openWeather(gson)

    @Singleton @JvmStatic
    @Provides fun openWeatherService(@OpenWeather retrofit: Retrofit): OpenWeatherMapService =
            retrofit.create(OpenWeatherMapService::class.java)
}

@Module object DatabaseModule {

    @Singleton @JvmStatic @Provides fun gson(): Gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, JsonSerializer<Date> { src, _, _ ->
                JsonPrimitive(src.time)
            })
            .registerTypeAdapter(Date::class.java, JsonDeserializer<Date> { jsonElement, _, _ ->
                Date(jsonElement.asJsonPrimitive.asLong)
            })
            .create()

    @Provides @JvmStatic @Singleton fun objectBox(@AppContext context: Context): BoxStore =
            MyObjectBox.builder().androidContext(context).build()

    @Provides @JvmStatic fun cityBox(objectBox: BoxStore): Box<CityEntity> =
            objectBox.boxFor(CityEntity::class.java)
}

@Module class CityListItemModule(private val entity: CityEntity) {

    @Provides fun cityEntity() = entity
}