package tomaszgorecki.simpleweather.activities

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.text.InputFilter
import android.widget.EditText
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Module
import dagger.Provides
import io.objectbox.Box
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_city_list.*
import kotlinx.android.synthetic.main.city_list.*
import timber.log.Timber
import tomaszgorecki.simpleweather.R
import tomaszgorecki.simpleweather.activities.base.BaseActivity
import tomaszgorecki.simpleweather.inject.ActivityModule
import tomaszgorecki.simpleweather.inject.CityListActivityComponent
import tomaszgorecki.simpleweather.inject.PerActivity
import tomaszgorecki.simpleweather.model.OpenWeatherCity
import tomaszgorecki.simpleweather.model.OpenWeatherCityEntity
import tomaszgorecki.simpleweather.model.OpenWeatherCityEntity_
import tomaszgorecki.simpleweather.model.OpenWeatherFindResult
import tomaszgorecki.simpleweather.network.OpenWeatherMapService
import tomaszgorecki.simpleweather.utils.queryChanges
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import javax.inject.Inject

class CityListActivity : BaseActivity(), FloatingSearchView.OnSearchListener {

    @Inject lateinit var weatherService: OpenWeatherMapService
    @Inject lateinit var rxPermissions: RxPermissions
    @Inject lateinit var cityBox: Box<OpenWeatherCityEntity>
    @Inject lateinit var adapter: CitiesRecyclerViewAdapter
    private lateinit var component: CityListActivityComponent
    private var searchingDisposable: Disposable? = null
    private var queryDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_list)
        component = getAppComponent().newCitiesActivityComponent(ActivityModule(this))
        component.inject(this)
        city_list.adapter = adapter
        setupFloatingSearchView()
        rxPermissions.request(Manifest.permission.INTERNET)
                .subscribe({ if (!it) finish() })
    }

    private fun setupFloatingSearchView() {
        floating_search_view.apply {
            setCloseSearchOnKeyboardDismiss(true)
            queryDisposable = queryChanges().skipInitialValue()
                    .debounce(300, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ newQuery: CharSequence -> run {
                        clearSearch()
                        if (newQuery.isBlank()) floating_search_view.clearSuggestions()
                        else {
                            searchingDisposable = weatherService.find(query = newQuery.toString())
                                    .doOnSubscribe({ floating_search_view.showProgress() })
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doFinally({ floating_search_view.hideProgress() })
                                    .subscribe(::onResponse, { Timber.e(it.message) })
                        }
                    }})
            setDismissFocusOnItemSelection(true)
            setOnSearchListener(this@CityListActivity)
            filterInput()
        }
    }

    private fun FloatingSearchView.filterInput() {
        val editText: EditText = findViewById(R.id.search_bar_text)
        val acceptedChars = Pattern.compile("[ a-zA-Z]")
        editText.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            source?.filter { acceptedChars.matcher(it.toString()).matches() } ?: ""
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        clearSearch()
        queryDisposable?.dispose()
        queryDisposable = null
    }

    private fun clearSearch() {
        searchingDisposable?.dispose()
        searchingDisposable = null
    }

    private fun onResponse(response: OpenWeatherFindResult) {
        Timber.d("Result $response")
        if (response.cities != null && response.cities.isNotEmpty()) {
            floating_search_view.swapSuggestions(response.cities)
        } else {
            floating_search_view.clearSuggestions()
        }
    }

    override fun onSearchAction(currentQuery: String?) {
    }

    override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {
        searchSuggestion?.let { onClicked(it as OpenWeatherCity)}
    }

    private fun onClicked(searched: OpenWeatherCity) {
        val list = cityBox.find(OpenWeatherCityEntity_.cityId, searched.id)
        val searchCity = if (list.isNotEmpty()) {
            list.first().update(searched)
        } else {
            searched.toEntity()
        }
        cityBox.put(searchCity)
        adapter.refresh()
        adapter.performClick(searchCity, false)
    }

}


@Module
abstract class CityListActivityModule {

    @Module
    companion object {
        @JvmStatic
        @Provides
        @PerActivity
        fun twoPanel(activity: Activity): PanelMode {
            return if (activity.city_detail_container != null) PanelMode.TWO_PANE
            else PanelMode.SINGLE
        }
    }
}

enum class PanelMode {
    SINGLE, TWO_PANE
}