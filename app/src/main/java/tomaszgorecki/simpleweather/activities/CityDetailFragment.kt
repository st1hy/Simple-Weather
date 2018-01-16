package tomaszgorecki.simpleweather.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_city_detail.*
import kotlinx.android.synthetic.main.city_detail.view.*
import tomaszgorecki.simpleweather.R
import tomaszgorecki.simpleweather.model.DummyContent

/**
 * A fragment representing a single City detail screen.
 * This fragment is either contained in a [CityListActivity]
 * in two-pane mode (on tablets) or a [CityDetailActivity]
 * on handsets.
 */
class CityDetailFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private var mItem: DummyContent.DummyItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                mItem = DummyContent.ITEM_MAP[it.getString(ARG_ITEM_ID)]
                activity?.toolbar_layout?.title = mItem?.content
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.city_detail, container, false)

        // Show the dummy content as text in a TextView.
        mItem?.let {
            rootView.city_detail.text = it.details
        }

        return rootView
    }

    companion object {
        const val ARG_ITEM_ID = "item_id"
    }
}
