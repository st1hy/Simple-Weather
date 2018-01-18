package tomaszgorecki.simpleweather.inject

import dagger.Subcomponent
import tomaszgorecki.simpleweather.activities.CityListActivity
import tomaszgorecki.simpleweather.activities.CityListActivityModule

@PerActivity
@Subcomponent(modules = arrayOf(ActivityModule::class, CityListActivityModule::class))
interface CityListActivityComponent {

    fun inject(activity: CityListActivity)

}