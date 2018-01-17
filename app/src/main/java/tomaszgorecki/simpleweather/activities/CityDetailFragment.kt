package tomaszgorecki.simpleweather.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_city_detail.*
import kotlinx.android.synthetic.main.city_detail.view.*
import tomaszgorecki.simpleweather.R
import tomaszgorecki.simpleweather.network.OpenWeatherCityEntity

class CityDetailFragment : Fragment() {

    private var mItem: OpenWeatherCityEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM)) {
                mItem = it.getParcelable(ARG_ITEM)
                activity?.toolbar_layout?.title = mItem?.name
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.city_detail, container, false)

        // Show the dummy content as text in a TextView.
        mItem?.let {
            rootView.city_detail.text = it.name
        }

        return rootView
    }

    companion object {
        const val ARG_ITEM = "item"
    }
}
