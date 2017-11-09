package org.zapto.hazgepszerv.hazgepszervlev_android.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gigamole.navigationtabstrip.NavigationTabStrip
import kotlinx.android.synthetic.main.app_bar_main.*
import org.zapto.hazgepszerv.hazgepszervlev_android.activities.MainActivity
import org.zapto.hazgepszerv.hazgepszervlev_android.R
import java.util.ArrayList


class TabbedJobreportsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.jobreport_tabs, container, false)

        val mainActivity = activity as MainActivity
        mainActivity.toolbar.title = "Hibajegyek"
        mainActivity.fab.visibility = View.VISIBLE

        val viewPager = view.findViewById<ViewPager>(R.id.viewpager)

        setupViewPager(viewPager)

        val navigationTabStrip = view.findViewById<NavigationTabStrip>(R.id.jobreports)
        navigationTabStrip.setTitles("Aktív","Lezárt")
        navigationTabStrip.setViewPager(viewPager, 0)
        return view
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = Adapter(childFragmentManager)
        adapter.addFragment(OpenedJobReportFragment())
        adapter.addFragment(ClosedJobreportFragment())
        viewPager.adapter = adapter
    }

    class Adapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList:ArrayList<Fragment> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment) {
            mFragmentList.add(fragment)
        }
    }
}