package org.zapto.hazgepszerv.hazgepszervlev_android.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.firebase.database.*
import org.zapto.hazgepszerv.hazgepszervlev_android.R
import org.zapto.hazgepszerv.hazgepszervlev_android.adapter.JobReportAdapter
import org.zapto.hazgepszerv.hazgepszervlev_android.model.JobReport
import java.util.*

class ClosedJobreportFragment : Fragment() {

    lateinit var ref : DatabaseReference
    val jobReports = ArrayList<JobReport>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater!!.inflate(R.layout.fragment_home, container, false)

        val recycleView = rootView.findViewById<RecyclerView>(R.id.jobreport_list)
        recycleView.hasFixedSize()

        recycleView.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)

        ref = FirebaseDatabase.getInstance().getReference("jobreports")

        ref.orderByChild("status").equalTo("closed").addValueEventListener( object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0!!.exists()){
                    jobReports.clear()
                    for (j in p0.children){
                        val jobreport = j.getValue(JobReport::class.java)
                        jobReports.add(jobreport!!)
                    }
                    val adapter = JobReportAdapter(jobReports, context)
                    recycleView.adapter = adapter
                }
            }
        })

        return rootView
    }
}// Required empty public constructor