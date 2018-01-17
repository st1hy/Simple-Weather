package tomaszgorecki.simpleweather.model

import android.content.Context
import dagger.Module
import dagger.Provides
import io.objectbox.Box
import io.objectbox.BoxStore
import tomaszgorecki.simpleweather.inject.AppContext
import tomaszgorecki.simpleweather.network.MyObjectBox
import tomaszgorecki.simpleweather.network.OpenWeatherCityEntity
import javax.inject.Singleton

@Module
abstract class DbModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        @Singleton
        fun objectBox(@AppContext context: Context): BoxStore {
            return MyObjectBox.builder().androidContext(context).build()
        }

        @JvmStatic
        @Provides
        fun citybox(objectBox: BoxStore): Box<OpenWeatherCityEntity> {
            return objectBox.boxFor(OpenWeatherCityEntity::class.java)
        }
    }
}