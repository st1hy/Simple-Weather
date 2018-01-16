package tomaszgorecki.simpleweather.app

import timber.log.Timber

class DebugWeatherApp : WeatherApp() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}