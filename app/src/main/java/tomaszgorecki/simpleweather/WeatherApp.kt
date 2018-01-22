package tomaszgorecki.simpleweather

import android.app.Application
import com.google.gson.Gson
import tomaszgorecki.simpleweather.inject.AppComponent
import tomaszgorecki.simpleweather.inject.DaggerAppComponent

open class WeatherApp : Application() {

    lateinit var component: AppComponent

    companion object {
        lateinit var gson: Lazy<Gson>

        @JvmStatic
        fun getComponent(app: Application): AppComponent {
            return (app as WeatherApp).component
        }
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder().app(this).create()
        gson = lazy { component.gson() }
    }

}