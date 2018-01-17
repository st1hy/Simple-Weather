package tomaszgorecki.simpleweather.utils

import android.databinding.BindingAdapter
import android.widget.TextView

class BindingUtils {

    companion object {
        @JvmStatic
        @BindingAdapter("android:text")
        fun setFloat(view: TextView, f: Float) {
            view.text = if (!f.isNaN()) {
                f.format(2)
            } else ""

        }
    }

}

fun Float.format(digits: Int) = String.format("%.${digits}f", this)