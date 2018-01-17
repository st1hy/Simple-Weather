package tomaszgorecki.simpleweather.activities

import android.Manifest
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.tbruyelle.rxpermissions2.RxPermissions
import io.objectbox.Box
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_city_list.*
import kotlinx.android.synthetic.main.city_list.*
import timber.log.Timber
import tomaszgorecki.simpleweather.R
import tomaszgorecki.simpleweather.activities.base.BaseActivity
import tomaszgorecki.simpleweather.inject.ActivityModule
import tomaszgorecki.simpleweather.network.*
import java.util.*
import javax.inject.Inject

class CityListActivity : BaseActivity(), FloatingSearchView.OnSearchListener {

    private var mTwoPane: Boolean = false
    @Inject lateinit var weatherService: OpenWeatherMapService
    @Inject lateinit var rxPermissions: RxPermissions
    @Inject lateinit var cityBox: Box<OpenWeatherCityEntity>
    var searchingDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_list)
        getAppComponent().newActivityComponent(ActivityModule(this)).inject(this)
        city_detail_container?.let { mTwoPane = true }
        setupRecyclerView(city_list)
        setupFloatingSearchView()

        rxPermissions.request(Manifest.permission.INTERNET).subscribe({ granted -> if (!granted) finish() })
    }

    private fun setupFloatingSearchView() {
        floating_search_view.apply {
            setCloseSearchOnKeyboardDismiss(true)
            setOnQueryChangeListener { _, newQuery -> run {
                    clearSearch()
                    if (newQuery.isNullOrBlank()) floating_search_view.clearSuggestions()
                    else {
                        searchingDisposable = weatherService.find(query = newQuery)
                                .doOnSubscribe({ floating_search_view.showProgress() })
                                .observeOn(AndroidSchedulers.mainThread())
                                .doFinally({ floating_search_view.hideProgress() })
                                .subscribe(::onResponse, { Timber.e(it.message) })
                    }
                }
            }
            setCloseSearchOnKeyboardDismiss(true)
            setDismissFocusOnItemSelection(true)
            setOnSearchListener(this@CityListActivity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearSearch()
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

    private fun onClicked(city: OpenWeatherCity) {
        val list = cityBox.find(OpenWeatherCityEntity_.cityId, city.id)
        val searchCity = if (list.isNotEmpty()) {
            val searchCity = list.first()
            searchCity.lastUsed = Date()
            searchCity
        } else {
            city.toEntity()
        }
        cityBox.put(searchCity)
        setupRecyclerView(city_list)
        (city_list.adapter as CitiesRecyclerViewAdapter).performClick(searchCity)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        val list = cityBox.query().sort({ o1, o2 -> o1.lastUsed.compareTo(o2.lastUsed) })
                .build().find()
        recyclerView.adapter = CitiesRecyclerViewAdapter(this, list, mTwoPane)
    }

}
