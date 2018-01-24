package tomaszgorecki.simpleweather.inject

import javax.inject.Qualifier
import javax.inject.Scope

@Scope annotation class PerApp

@Scope annotation class PerActivity

@Scope annotation class PerListItem

@Qualifier annotation class AppContext

@Qualifier annotation class ActivityContext

@Qualifier annotation class OpenWeather