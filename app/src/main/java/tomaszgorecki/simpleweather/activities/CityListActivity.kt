package tomaszgorecki.simpleweather.activities

import android.Manifest
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.google.common.collect.Collections2
import com.google.common.collect.Iterables
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_city_list.*
import kotlinx.android.synthetic.main.city_list.*
import timber.log.Timber
import tomaszgorecki.simpleweather.R
import tomaszgorecki.simpleweather.activities.base.BaseActivity
import tomaszgorecki.simpleweather.inject.ActivityModule
import tomaszgorecki.simpleweather.model.DummyContent
import tomaszgorecki.simpleweather.network.AccuweatherService
import tomaszgorecki.simpleweather.network.CityCandidates
import java.util.*
import javax.inject.Inject

class CityListActivity : BaseActivity() {

    private var mTwoPane: Boolean = false
    @Inject lateinit var weatherService: AccuweatherService
    @Inject lateinit var rxPermissions: RxPermissions
    var searchingDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_list)
        getAppComponent().newActivityComponent(ActivityModule(this)).inject(this)
        city_detail_container?.let { mTwoPane = true }
        setupRecyclerView(city_list)
        floating_search_view.setCloseSearchOnKeyboardDismiss(true)
        floating_search_view.setOnQueryChangeListener { oldQuery, newQuery -> run {
            searchingDisposable?.dispose()
            if (newQuery.isNullOrBlank()) floating_search_view.clearSuggestions()
            else {
                val langCode = Locale.getDefault().language
                searchingDisposable = weatherService.autompleteCity(languageCode = langCode, query = newQuery)
                        .doOnSubscribe({ floating_search_view.showProgress() })
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally({ floating_search_view.hideProgress() })
                        .subscribe(::onResponse, Timber::e)
            }
        }}

        rxPermissions.request(Manifest.permission.INTERNET).subscribe({ granted -> if (!granted) finish() })
    }

    override fun onDestroy() {
        super.onDestroy()
        searchingDisposable?.dispose()
    }

    private fun onResponse(response: Collection<CityCandidates>) {
        Timber.d(Iterables.toString(response))
        val suggestions = Collections2.transform(response, { r -> r.getSuggestion() })
        floating_search_view.swapSuggestions(suggestions.toMutableList())
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane)
    }

}
