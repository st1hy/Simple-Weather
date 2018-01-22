package tomaszgorecki.simpleweather.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_city_detail.*
import tomaszgorecki.simpleweather.R

class CityDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_detail)
        setSupportActionBar(detail_toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            val fragment = CityDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CityDetailFragment.ARG_ITEM,
                            intent.getParcelableExtra(CityDetailFragment.ARG_ITEM))
                }
            }

            supportFragmentManager.beginTransaction()
                    .add(R.id.city_detail_container, fragment)
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    navigateUpTo(Intent(this, CityListActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}
