package org.zapto.hazgepszerv.hazgepszervlev_android.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.zapto.hazgepszerv.hazgepszervlev_android.R
import org.zapto.hazgepszerv.hazgepszervlev_android.activities.MainActivity
import org.zapto.hazgepszerv.hazgepszervlev_android.adapter.JobReportCardAdapter
import org.zapto.hazgepszerv.hazgepszervlev_android.model.JobReport
import org.zapto.hazgepszerv.hazgepszervlev_android.utils.DateTimeFormatter
import java.util.*

class HomeScreenFragment : Fragment() {

    lateinit var ref : DatabaseReference
    val jobReports = ArrayList<JobReport>()
    var today = Date()
    val dtFormatter = DateTimeFormatter()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater!!.inflate(R.layout.home_screen_fragment, container, false)

        val formattedToday = dtFormatter.ddf.format(today)

        val mainActivity = activity as MainActivity
        mainActivity.toolbar.title = "Háztartási Gépszerviz"

        val allbutton = rootView.findViewById<Button>(R.id.allbutton)
        val noimage = rootView.findViewById<TextView>(R.id.noimage)

        var i = 0

        allbutton.setOnClickListener {
            val asd = TabbedJobreportsFragment::class.java.newInstance()
            val fragmentManager = fragmentManager
            fragmentManager.beginTransaction().replace(R.id.flContent, asd).addToBackStack(asd.toString()).commit()
        }

        val recyclerView = rootView.findViewById<RecyclerView>(R.id.jobreport_list)
        recyclerView.hasFixedSize()

        recyclerView.layoutManager = GridLayoutManager(activity, 2)

        ref = FirebaseDatabase.getInstance().getReference("jobreports")

        ref.keepSynced(true)

        ref.orderByChild("status").equalTo("open").addValueEventListener( object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0!!.exists()){
                    jobReports.clear()
                    for (j in p0.children){
                        val jobreport = j.getValue(JobReport::class.java)
                        val reportDate = dtFormatter.getDate(jobreport?.planned_delivery!!)
                        if (reportDate.equals(formattedToday) && i < 4) {
                            jobReports.add(jobreport)
                            i++
                        }

                        if (jobReports.isEmpty()){
                            recyclerView.visibility = View.GONE
                            noimage.visibility = View.VISIBLE
                        } else {
                            recyclerView.visibility = View.VISIBLE
                            noimage.visibility = View.GONE
                        }
                    }
                    val adapter = JobReportCardAdapter(jobReports, context)
                    recyclerView.adapter = adapter
                }
            }
        })

        return rootView
    }
}