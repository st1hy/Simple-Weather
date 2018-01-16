package tomaszgorecki.simpleweather.activities.base

import android.support.v7.app.AppCompatActivity
import tomaszgorecki.simpleweather.app.WeatherApp
import tomaszgorecki.simpleweather.inject.AppComponent

abstract class BaseActivity: AppCompatActivity() {

    protected fun getAppComponent(): AppComponent {
        return WeatherApp.getComponent(application)
    }
}
