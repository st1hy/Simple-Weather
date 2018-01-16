package tomaszgorecki.simpleweather.inject

import dagger.Subcomponent
import tomaszgorecki.simpleweather.activities.CityListActivity

@PerActivity
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

    fun inject(activity: CityListActivity)

}