package tomaszgorecki.simpleweather.inject

import javax.inject.Qualifier
import javax.inject.Scope

@Scope
annotation class PerApp

@Scope
annotation class PerActivity

@Qualifier
annotation class AppContext

@Qualifier
annotation class ActivityContext