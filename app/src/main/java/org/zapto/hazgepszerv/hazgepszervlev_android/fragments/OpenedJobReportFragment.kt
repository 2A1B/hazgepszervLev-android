package org.zapto.hazgepszerv.hazgepszervlev_android.fragments

import android.graphics.Canvas
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.firebase.database.*
import org.zapto.hazgepszerv.hazgepszervlev_android.R
import org.zapto.hazgepszerv.hazgepszervlev_android.adapter.JobReportAdapter
import org.zapto.hazgepszerv.hazgepszervlev_android.adapters.RecyclerItemTouchAdapter
import org.zapto.hazgepszerv.hazgepszervlev_android.model.JobReport
import java.util.*

class OpenedJobReportFragment : Fragment() , RecyclerItemTouchAdapter.RecyclerItemTouchHelperListener {

    lateinit var ref : DatabaseReference
    val jobReports = ArrayList<JobReport>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater!!.inflate(R.layout.fragment_home, container, false)

        val recyclerView = rootView.findViewById<RecyclerView>(R.id.jobreport_list)
        recyclerView.hasFixedSize()

        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)

        ref = FirebaseDatabase.getInstance().getReference("jobreports")

        ref.orderByChild("status").equalTo("open").addValueEventListener( object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0!!.exists()){
                    jobReports.clear()
                    for (j in p0.children){
                        val jobreport = j.getValue(JobReport::class.java)
                        jobReports.add(jobreport!!)
                    }
                    val adapter = JobReportAdapter(jobReports)
                    recyclerView.adapter = adapter
                }
            }
        })
        Log.d("OpenedJobReportFragment","HELLOOOOOO")
        Log.d("OpenedJobReportFragment", Integer.toString(jobReports.size))


        val itemTouchHelperCallback = RecyclerItemTouchAdapter(0, ItemTouchHelper.RIGHT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)

        val itemTouchHelperCallback1 = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(recyclerView)


        return rootView
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        updateJobReportStatus(position)
    }

    private fun updateJobReportStatus(position: Int) {
        ref.child(jobReports[position].uuid).child("status").setValue("closed")
    }
}// Required empty public constructor