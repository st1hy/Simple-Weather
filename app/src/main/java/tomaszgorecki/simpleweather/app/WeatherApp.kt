package tomaszgorecki.simpleweather.app

import android.app.Application
import tomaszgorecki.simpleweather.inject.AppComponent
import tomaszgorecki.simpleweather.inject.DaggerAppComponent

class WeatherApp : Application() {

    lateinit var component: AppComponent

    companion object {
        @JvmStatic fun getComponent(app: Application): AppComponent {
            return (app as WeatherApp).component!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder().app(this).create()
    }

}