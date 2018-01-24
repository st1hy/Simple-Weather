package tomaszgorecki.simpleweather.model

import dagger.Lazy
import io.objectbox.Box
import io.objectbox.rx.RxQuery
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import tomaszgorecki.simpleweather.inject.PerApp
import tomaszgorecki.simpleweather.utils.durationTill
import tomaszgorecki.simpleweather.utils.now
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerApp class ContentProvider @Inject constructor() {

    @Inject lateinit var weatherService: OpenWeatherMapService
    @Inject lateinit var cityBox: Lazy<Box<CityEntity>>

    private fun cityBox() : Box<CityEntity> = cityBox.get()

    fun addOrUpdateFrom(city: City): Single<CityEntity> = Single.just(city.toEntity()).map { entity ->
        val list = cityBox().find(CityEntity_.cityId, entity.cityId)
        if (list.isNotEmpty()) {
            entity.id = list.first().id
        }
        cityBox().put(entity)
        entity
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    /**
     * Updates weather in the city if necessary and returns a updated weather
     * Provides single result on main thread
     */
    fun updateWeather(entity: CityEntity): Single<CityEntity> {
        return if (shouldUpdateWeather(entity)) {
            weatherService.weather(cityId = entity.cityId)
                    .map({ cityWeather ->
                        entity.update(cityWeather)
                        cityBox().put(entity)
                        entity
                    })
                    .observeOn(AndroidSchedulers.mainThread())
        } else {
            Single.just(entity)
                    .map { entity1 ->
                        entity1.lastUsed = Date()
                        cityBox().put(entity1)
                        entity1
                    }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    private fun shouldUpdateWeather(entity: CityEntity): Boolean {
        val relevantTime = entity.lastUpdated
        return if (relevantTime == null) {
            true
        } else {
            relevantTime.durationTill(now(), TimeUnit.MINUTES) > 10
        }
    }

    fun queryCachedWeather(): Observable<List<CityEntity>> = RxQuery.observable(
            cityBox().query()
                    .sort { o1, o2 -> o2.lastUsed.compareTo(o1.lastUsed) }
                    .build()
    ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


    enum class SearchingState {
        UNCHANGED, SEARCHING, IDLE
    }

    fun queryToSearchResult(onStateChanged: (SearchingState) -> Unit)
            = ObservableTransformer<CharSequence, List<City>> { source ->
        source.switchMapSingle { newQuery ->
            if (newQuery.isNotBlank()) {
                weatherService.find(query = newQuery.toString())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { onStateChanged.invoke(SearchingState.SEARCHING) }
                        .doFinally { onStateChanged.invoke(SearchingState.IDLE) }
                        .map { result -> result.cities ?: emptyList() }
                        .onErrorReturn { error ->
                            Timber.e(error.message)
                            emptyList()
                        }
            } else Single.just(emptyList())
        }
    }
}