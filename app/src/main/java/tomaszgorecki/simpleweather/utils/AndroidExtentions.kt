package tomaszgorecki.simpleweather.utils

import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.bumptech.glide.Glide
import tomaszgorecki.simpleweather.WeatherApp
import tomaszgorecki.simpleweather.inject.AppComponent
import java.util.*
import java.util.concurrent.TimeUnit

fun ImageView.setUrl(url: String?) {
    Glide.with(this).load(url).into(this)
}

fun AppCompatActivity.getAppComponent(): AppComponent {
    return WeatherApp.getComponent(application)
}

fun Date.durationTill(other: Date, timeUnit: TimeUnit): Long {
    return timeUnit.convert(Math.abs(time - other.time), TimeUnit.MILLISECONDS)
}

fun now(): Date = Date()
