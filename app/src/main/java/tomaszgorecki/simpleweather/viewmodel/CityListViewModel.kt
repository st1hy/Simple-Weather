package tomaszgorecki.simpleweather.viewmodel

import android.databinding.ObservableField
import android.text.InputFilter
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import tomaszgorecki.simpleweather.inject.PerActivity
import tomaszgorecki.simpleweather.model.City
import tomaszgorecki.simpleweather.model.CityEntity
import tomaszgorecki.simpleweather.model.ContentProvider
import tomaszgorecki.simpleweather.model.ContentProvider.SearchingState
import tomaszgorecki.simpleweather.view.Navigator
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import javax.inject.Inject

@PerActivity class CityListViewModel @Inject constructor() : BaseViewModel {

    @Inject lateinit var contentProvider: ContentProvider
    @Inject lateinit var navigator: Navigator

    val suggestions: ObservableField<List<SearchSuggestion>> = ObservableField(emptyList())
    val searchProgress: ObservableField<SearchingState> = ObservableField(SearchingState.UNCHANGED)
    val cities: ObservableField<List<CityEntity>> by lazy { initializeCityList() }

    private fun initializeCityList(): ObservableField<List<CityEntity>> {
        val cities = ObservableField<List<CityEntity>>(emptyList())
        disposables.add(contentProvider.queryCachedWeather()
                .subscribe { list ->
                    cities.set(list)
                })
        return cities
    }

    private val disposables = CompositeDisposable()

    fun queryChanges(queries: Observable<CharSequence>) {
        disposables.add(queries.debounce(300, TimeUnit.MILLISECONDS)
                .compose(contentProvider.queryToSearchResult { state -> searchProgress.set(state) })
                .subscribe({ cities -> suggestions.set(cities) },
                        { error -> Timber.e(error.message) }))
    }

    fun inputFilters(): Array<InputFilter> {
        val acceptedChars = Pattern.compile("[ a-zA-Z]")
        return arrayOf(InputFilter { source, _, _, _, _, _ ->
            source?.filter { character ->
                acceptedChars.matcher(character.toString()).matches()
            } ?: ""
        })
    }

    fun searchListener() = object : FloatingSearchView.OnSearchListener {
        override fun onSearchAction(currentQuery: String?) {
        }

        override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {
            searchSuggestion?.let { suggestion ->
                disposables.add(contentProvider.addOrUpdateFrom(suggestion as City)
                        .subscribe { entity -> navigator.navigateToDetail(entity) })
            }
        }
    }

    override fun onDestroy() {
        disposables.clear()
    }

}