package tomaszgorecki.simpleweather

import android.app.ListActivity
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle

class SearchActivity: ListActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            doMySearch(query)
        }
    }

    private fun doMySearch(query: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSearchRequested(): Boolean {
        return super.onSearchRequested()
    }
}