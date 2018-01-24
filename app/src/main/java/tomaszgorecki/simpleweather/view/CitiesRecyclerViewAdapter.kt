package tomaszgorecki.simpleweather.view

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import tomaszgorecki.simpleweather.R
import tomaszgorecki.simpleweather.databinding.CityListContentBinding
import tomaszgorecki.simpleweather.inject.CityListItemComponentBuilder
import tomaszgorecki.simpleweather.inject.CityListItemModule
import tomaszgorecki.simpleweather.inject.PerActivity
import tomaszgorecki.simpleweather.model.CityEntity
import javax.inject.Inject

@PerActivity class CitiesRecyclerViewAdapter @Inject constructor() :
        RecyclerView.Adapter<CitiesRecyclerViewAdapter.ViewHolder>() {

    @Inject lateinit var builder: CityListItemComponentBuilder

    var values: List<CityEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : CityListContentBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.city_list_content,
                parent,
                false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val module = CityListItemModule(values[position])
        holder.binding.viewModel = builder.newCityListItemComponent(module).newViewModel()
    }

    override fun getItemCount(): Int {
        return values.size
    }

    class ViewHolder(val binding: CityListContentBinding) : RecyclerView.ViewHolder(binding.root)

}