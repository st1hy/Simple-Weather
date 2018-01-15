package tomaszgorecki.simpleweather.inject

import android.content.Context
import dagger.Binds
import dagger.Module
import tomaszgorecki.simpleweather.app.WeatherApp

@Module
abstract class AppModule {

    @Binds
    @AppContext
    @PerApp
    abstract fun appContext(app: WeatherApp): Context
}