package tomaszgorecki.simpleweather.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputFilter
import android.widget.EditText
import com.arlib.floatingsearchview.FloatingSearchView
import kotlinx.android.synthetic.main.city_list.*
import tomaszgorecki.simpleweather.R
import tomaszgorecki.simpleweather.databinding.ActivityCityListBinding
import tomaszgorecki.simpleweather.inject.ActivityModule
import tomaszgorecki.simpleweather.utils.CitiesRecyclerViewAdapter
import tomaszgorecki.simpleweather.utils.getAppComponent
import tomaszgorecki.simpleweather.viewmodel.CityListViewModel
import java.util.regex.Pattern
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


    private fun FloatingSearchView.filterInput() {
        val editText: EditText = findViewById(R.id.search_bar_text)
        val acceptedChars = Pattern.compile("[ a-zA-Z]")
        editText.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            source?.filter { acceptedChars.matcher(it.toString()).matches() } ?: ""
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }


}

