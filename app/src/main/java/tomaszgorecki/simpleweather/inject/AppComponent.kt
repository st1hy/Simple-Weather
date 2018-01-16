package tomaszgorecki.simpleweather.inject

import dagger.BindsInstance
import dagger.Component
import tomaszgorecki.simpleweather.app.WeatherApp
import tomaszgorecki.simpleweather.network.NetworkModule
import javax.inject.Singleton

@Singleton
@PerApp
@Component(modules = arrayOf(AppModule::class, NetworkModule::class))
interface AppComponent {

    fun newActivityComponent(activity: ActivityModule): ActivityComponent

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun app(app: WeatherApp): Builder

        fun create(): AppComponent
    }
}