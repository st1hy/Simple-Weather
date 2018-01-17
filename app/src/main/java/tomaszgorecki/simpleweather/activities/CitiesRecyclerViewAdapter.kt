package tomaszgorecki.simpleweather.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.objectbox.Box
import kotlinx.android.synthetic.main.city_list_content.view.*
import tomaszgorecki.simpleweather.R
import tomaszgorecki.simpleweather.model.OpenWeatherCityEntity
import javax.inject.Inject

class CitiesRecyclerViewAdapter @Inject constructor() :
        RecyclerView.Adapter<CitiesRecyclerViewAdapter.ViewHolder>() {

    @Inject lateinit var activity: AppCompatActivity
    @Inject lateinit var mode: PanelMode
    @Inject lateinit var cityBox: Box<OpenWeatherCityEntity>
    val values: List<OpenWeatherCityEntity> by lazy {
        cityBox.query().sort({ o1, o2 -> o2.lastUsed.compareTo(o1.lastUsed) })
            .build().find().toList()
    }

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as OpenWeatherCityEntity
            performClick(item)
        }
    }

    fun performClick(item: OpenWeatherCityEntity) {
        if (mode == PanelMode.TWO_PANE) {
            val fragment = CityDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CityDetailFragment.ARG_ITEM, item)
                }
            }
            activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.city_detail_container, fragment)
                    .commit()
        } else {
            val intent = Intent(activity, CityDetailActivity::class.java).apply {
                putExtra(CityDetailFragment.ARG_ITEM, item)
            }
            activity.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.city_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position].also {
            holder.name.text = "${it.name}, ${it.city?.sys?.country}"
        }

        with(holder.itemView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int {
        return values.size
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val name: TextView = mView.city_name
    }
}