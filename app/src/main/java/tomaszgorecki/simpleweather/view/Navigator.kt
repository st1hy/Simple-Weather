package tomaszgorecki.simpleweather.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import tomaszgorecki.simpleweather.R
import tomaszgorecki.simpleweather.inject.PanelMode
import tomaszgorecki.simpleweather.inject.PerActivity
import tomaszgorecki.simpleweather.model.CityEntity
import javax.inject.Inject

@PerActivity class Navigator @Inject constructor() {

    @Inject lateinit var mode: PanelMode
    @Inject lateinit var activity: Activity
    @Inject lateinit var fragmentManager: FragmentManager

    fun navigateToDetail(item: CityEntity) {
        if (mode == PanelMode.TWO_PANE) {
            val fragment = CityDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CityDetailFragment.ARG_ITEM, item)
                }
            }
            fragmentManager.beginTransaction()
                    .replace(R.id.city_detail_container, fragment)
                    .commit()
        } else {
            val intent = Intent(activity, CityDetailActivity::class.java).apply {
                putExtra(CityDetailFragment.ARG_ITEM, item)
            }
            activity.startActivity(intent)
        }
    }

}