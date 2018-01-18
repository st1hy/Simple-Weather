package tomaszgorecki.simpleweather.utils

import android.databinding.BindingAdapter
import android.databinding.BindingConversion
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

object BindingUtils {

    @JvmStatic
    @BindingAdapter("android:text")
    fun setFloat(view: TextView, f: Float) {
        view.text = if (!f.isNaN()) f.format(2) else ""
    }

    @JvmStatic
    @BindingAdapter("app:url")
    fun setImageViewUrl(view: ImageView, url: String?) = view.setUrl(url)

    @JvmStatic
    @BindingConversion
    fun convertBooleanToVisibility(isVisible: Boolean): Int {
        return if (isVisible) View.VISIBLE else View.GONE
    }

}

fun Float.format(digits: Int) = String.format("%.${digits}f", this)

fun ImageView.setUrl(url: String?) {
    Glide.with(this).load(url).into(this)
}