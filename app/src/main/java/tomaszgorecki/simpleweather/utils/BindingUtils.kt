package tomaszgorecki.simpleweather.utils

import android.databinding.BindingAdapter
import android.databinding.BindingConversion
import android.view.View
import android.widget.ImageView
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import io.reactivex.Observable
import tomaszgorecki.simpleweather.viewmodel.CityListViewModel.SearchingState

object BindingUtils {

    @BindingAdapter("app:url") @JvmStatic
    fun setImageViewUrl(view: ImageView, url: String?) = view.setUrl(url)

    @BindingConversion @JvmStatic
    fun convertBooleanToVisibility(isVisible: Boolean): Int {
        return if (isVisible) View.VISIBLE else View.GONE
    }

    @BindingAdapter("queryChanges") @JvmStatic
    fun queryChanges(view: FloatingSearchView, fn: SearchConsumer) = fn.accept(view.queryChanges())

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

    interface SearchConsumer {
        fun accept(observable: Observable<CharSequence>)
    }

}
