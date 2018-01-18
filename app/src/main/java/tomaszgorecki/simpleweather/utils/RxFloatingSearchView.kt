package tomaszgorecki.simpleweather.utils

import android.support.annotation.CheckResult
import com.arlib.floatingsearchview.FloatingSearchView
import com.jakewharton.rxbinding2.InitialValueObservable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

/**
 * Source:
 * https://github.com/arimorty/floatingsearchview
 *
 * https://github.com/arimorty/floatingsearchview/tree/master/library-rxbinding
 * https://github.com/arimorty/floatingsearchview/tree/master/library-rxbinding-kotlin
 *
 * Licence: Apache License 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
object RxFloatingSearchView {

    @CheckResult
    @JvmOverloads
    fun queryChanges(view: FloatingSearchView,
                     characterLimit: Int = 1): InitialValueObservable<CharSequence> {
        return QueryObservable(view, characterLimit)
    }

}

fun FloatingSearchView.queryChanges(minQueryLimit: Int = 1): InitialValueObservable<CharSequence> =
        RxFloatingSearchView.queryChanges(this, minQueryLimit)

private class QueryObservable(
        private val view: FloatingSearchView,
        private val minQueryLength: Int = 1) : InitialValueObservable<CharSequence>() {

    override fun subscribeListener(observer: Observer<in CharSequence>) {
        val listener = Listener(view, observer, minQueryLength)
        observer.onSubscribe(listener)
        view.setOnQueryChangeListener(listener)
    }

    override fun getInitialValue(): CharSequence {
        return view.query
    }

    internal class Listener(
            private val view: FloatingSearchView,
            private val observer: Observer<in CharSequence>,
            private val minQueryLength: Int) : MainThreadDisposable(),
            FloatingSearchView.OnQueryChangeListener {

        override fun onSearchTextChanged(oldQuery: String, newQuery: String?) {
            if (!isDisposed && newQuery != null && newQuery.length >= minQueryLength) {
                observer.onNext(newQuery)
            }
        }

        override fun onDispose() {
            view.setOnQueryChangeListener(null)
        }
    }
}
