package tomaszgorecki.simpleweather.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.city_list.*
import tomaszgorecki.simpleweather.R
import tomaszgorecki.simpleweather.databinding.ActivityCityListBinding
import tomaszgorecki.simpleweather.inject.ActivityModule
import tomaszgorecki.simpleweather.utils.getAppComponent
import tomaszgorecki.simpleweather.viewmodel.CityListViewModel
import javax.inject.Inject

class CityListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCityListBinding
    @Inject lateinit var viewModel: CityListViewModel
    @Inject lateinit var adapter: CitiesRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_city_list)
        getAppComponent().newCitiesActivityComponent(ActivityModule(this)).inject(this)
        binding.viewModel = viewModel
        city_list.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }


}

