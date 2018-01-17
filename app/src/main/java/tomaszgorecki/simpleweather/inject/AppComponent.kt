package tomaszgorecki.simpleweather.inject

import com.google.gson.Gson
import dagger.BindsInstance
import dagger.Component
import tomaszgorecki.simpleweather.app.WeatherApp
import tomaszgorecki.simpleweather.model.DbModule
import tomaszgorecki.simpleweather.network.NetworkModule
import javax.inject.Singleton

@Singleton
@PerApp
@Component(modules = arrayOf(AppModule::class, NetworkModule::class, DbModule::class))
interface AppComponent {

    fun newActivityComponent(activity: ActivityModule): CityListActivityComponent
    fun gson(): Gson

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun app(app: WeatherApp): Builder

        fun create(): AppComponent
    }
}