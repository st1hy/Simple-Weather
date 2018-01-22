package tomaszgorecki.simpleweather.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_city_detail.*
import tomaszgorecki.simpleweather.R
import tomaszgorecki.simpleweather.databinding.CityDetailBinding
import tomaszgorecki.simpleweather.model.OpenWeatherCityEntity

class CityDetailFragment : Fragment() {

    private var entity: OpenWeatherCityEntity? = null
    private lateinit var binding: CityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM)) {
                entity = it.getParcelable(ARG_ITEM)
                activity?.toolbar_layout?.title = entity?.name
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.city_detail, container, false)
        binding.city = entity?.city
        return binding.root
    }

    companion object {
        const val ARG_ITEM = "item"
    }
}
