package tomaszgorecki.simpleweather.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.city_list_content.view.*
import tomaszgorecki.simpleweather.R
import tomaszgorecki.simpleweather.network.OpenWeatherCityEntity


class CitiesRecyclerViewAdapter(private val mParentActivity: CityListActivity,
                                private val mValues: List<OpenWeatherCityEntity>,
                                private val mTwoPane: Boolean) :
        RecyclerView.Adapter<CitiesRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as OpenWeatherCityEntity
            performClick(item)
        }
    }

    fun performClick(item: OpenWeatherCityEntity) {
        if (mTwoPane) {
            val fragment = CityDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CityDetailFragment.ARG_ITEM, item)
                }
            }
            mParentActivity.supportFragmentManager.beginTransaction().replace(R.id.city_detail_container, fragment).commit()
        } else {
            val intent = Intent(mParentActivity, CityDetailActivity::class.java).apply {
                putExtra(CityDetailFragment.ARG_ITEM, item)
            }
            mParentActivity.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.city_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.name.text = item.name

        with(holder.itemView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val name: TextView = mView.city_name
    }
}