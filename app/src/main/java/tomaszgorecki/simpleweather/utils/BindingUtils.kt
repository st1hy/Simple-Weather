package tomaszgorecki.simpleweather.utils

import android.databinding.BindingAdapter
import android.databinding.BindingConversion
import android.support.v7.widget.RecyclerView
import android.text.InputFilter
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import io.reactivex.Observable
import tomaszgorecki.simpleweather.R
import tomaszgorecki.simpleweather.model.CityEntity
import tomaszgorecki.simpleweather.model.ContentProvider.SearchingState
import tomaszgorecki.simpleweather.view.CitiesRecyclerViewAdapter

object BindingUtils {

    @BindingAdapter("url") @JvmStatic
    fun setImageViewUrl(view: ImageView, url: String?) = view.setUrl(url)

    @BindingConversion @JvmStatic
    fun convertBooleanToVisibility(isVisible: Boolean): Int {
        return if (isVisible) View.VISIBLE else View.GONE
    }

    @BindingAdapter("queryChanges") @JvmStatic
    fun queryChanges(view: FloatingSearchView, fn: SearchConsumer) = fn.accept(view.queryChanges())

    interface SearchConsumer {
        fun accept(observable: Observable<CharSequence>)
    }

    @BindingAdapter("suggestions") @JvmStatic
    fun setSuggestions(view: FloatingSearchView, list: List<SearchSuggestion>) =
            view.swapSuggestions(list)

    @BindingAdapter("searchProgress") @JvmStatic
    fun setProgress(view: FloatingSearchView, progress: SearchingState) =
            when (progress) {
                SearchingState.SEARCHING -> view.showProgress()
                SearchingState.IDLE -> view.hideProgress()
                else -> {}
            }

    @BindingAdapter("inputFilters") @JvmStatic
    fun inputFilters(view: FloatingSearchView, filters: Array<InputFilter>) {
        val editText: EditText = view.findViewById(R.id.search_bar_text)
        editText.filters = filters
    }

    @BindingAdapter("cityList") @JvmStatic
    fun cityList(view: RecyclerView, cities: List<CityEntity>) {
        val adapter = view.adapter as CitiesRecyclerViewAdapter
        adapter.values = cities
        adapter.notifyDataSetChanged()
    }
}
