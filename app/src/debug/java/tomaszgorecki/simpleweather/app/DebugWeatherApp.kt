package tomaszgorecki.simpleweather.app

import timber.log.Timber
import tomaszgorecki.simpleweather.WeatherApp

class DebugWeatherApp : WeatherApp() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}