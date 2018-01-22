package tomaszgorecki.simpleweather.viewmodel

import android.databinding.ObservableField
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.google.common.base.Optional
import io.objectbox.Box
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import timber.log.Timber
import tomaszgorecki.simpleweather.inject.PerActivity
import tomaszgorecki.simpleweather.model.*
import tomaszgorecki.simpleweather.utils.CitiesRecyclerViewAdapter
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerActivity
class CityListViewModel @Inject constructor() : BaseViewModel {

    @Inject lateinit var weatherService: OpenWeatherMapService
    @Inject lateinit var cityBox: Box<OpenWeatherCityEntity>
    @Inject lateinit var adapter: CitiesRecyclerViewAdapter

    val suggestions: ObservableField<List<SearchSuggestion>> = ObservableField(emptyList())
    val searchProgress: ObservableField<SearchingState> = ObservableField(SearchingState.UNCHANGED)

    private var queryDisposable: Disposable? = null

    fun queryChanges(observable: Observable<CharSequence>) {
        queryDisposable = observable.debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .switchMapSingle { newQuery ->
                    if (newQuery.isNotBlank()) {
                        weatherService.find(query = newQuery.toString())
                                .doOnSubscribe { searchProgress.set(SearchingState.SEARCHING) }
                                .observeOn(AndroidSchedulers.mainThread())
                                .doFinally { searchProgress.set(SearchingState.IDLE) }
                                .map { Optional.of(it) }
                                .onErrorReturn { error ->
                                    Timber.e(error.message)
                                    Optional.absent()
                                }
                    } else Single.just(Optional.absent<OpenWeatherFindResult>())
                }
                .subscribe(::onResponse, { Timber.e(it.message) })
    }

    private fun onResponse(response: Optional<OpenWeatherFindResult>) {
        val cities = response.orNull()?.cities ?: emptyList()
        suggestions.set(cities)
    }

    fun searchListener() = object : FloatingSearchView.OnSearchListener {
        override fun onSearchAction(currentQuery: String?) {
        }

        override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {
            searchSuggestion?.let { onClicked(it as OpenWeatherCity) }
        }
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

    override fun onDestroy() {
        queryDisposable?.dispose()
        queryDisposable = null
    }

    enum class SearchingState {
        UNCHANGED, SEARCHING, IDLE
    }
}