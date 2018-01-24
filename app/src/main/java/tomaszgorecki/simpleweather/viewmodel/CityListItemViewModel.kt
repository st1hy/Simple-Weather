package tomaszgorecki.simpleweather.viewmodel

import android.view.View
import tomaszgorecki.simpleweather.inject.PerListItem
import tomaszgorecki.simpleweather.model.CityEntity
import tomaszgorecki.simpleweather.model.ContentProvider
import tomaszgorecki.simpleweather.view.Navigator
import javax.inject.Inject

@PerListItem class CityListItemViewModel @Inject constructor() {

    @Inject lateinit var navigator: Navigator
    @Inject lateinit var contentProvider: ContentProvider
    @Inject lateinit var entity: CityEntity

    fun name() = entity.city.name ?: ""

    @Suppress("UNUSED_PARAMETER")
    fun onClick(_view: View) {
        contentProvider.updateWeather(entity)
                .subscribe({ entity -> navigator.navigateToDetail(entity)})
    }

}