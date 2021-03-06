package tomaszgorecki.simpleweather.inject

import com.google.gson.Gson
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import tomaszgorecki.simpleweather.WeatherApp
import tomaszgorecki.simpleweather.view.CityListActivity
import tomaszgorecki.simpleweather.viewmodel.CityListItemViewModel
import javax.inject.Singleton

@Component(modules = arrayOf(AppModule::class))
@Singleton @PerApp interface AppComponent {

    fun newCitiesActivityComponent(activity: ActivityModule): CityListActivityComponent
    fun gson(): Gson

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun app(app: WeatherApp): Builder

        fun create(): AppComponent
    }
}

@Subcomponent(modules = arrayOf(ActivityModule::class, CityListActivityModule::class))
@PerActivity interface CityListActivityComponent : CityListItemComponentBuilder {

    fun inject(activity: CityListActivity)

}

@Subcomponent(modules = arrayOf(CityListItemModule::class))
@PerListItem interface CityListItemComponent {

    fun newViewModel() : CityListItemViewModel

}

interface CityListItemComponentBuilder {
    fun newCityListItemComponent(module: CityListItemModule): CityListItemComponent
}