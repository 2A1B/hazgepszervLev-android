package org.zapto.hazgepszerv.hazgepszervlev_android.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import org.zapto.hazgepszerv.hazgepszervlev_android.R
import org.zapto.hazgepszerv.hazgepszervlev_android.model.JobReport
import org.zapto.hazgepszerv.hazgepszervlev_android.utils.DateTimeFormatter
import java.util.*

class JobReportAdapter(val jobreports : ArrayList<JobReport>) : RecyclerView.Adapter<JobReportAdapter.ViewHolder>() {

    private val dateTimeFormatter : DateTimeFormatter = DateTimeFormatter()

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val jobreport : JobReport = jobreports[position]

        holder?.customer_name?.text = jobreport.customer_name
        holder?.customer_address?.text = jobreport.customer_address
        holder?.customer_phone?.text = jobreport.customer_phone
        holder?.timestamp?.text = dateTimeFormatter.getTime(jobreport.planned_delivery)
        holder?.datestamp?.text = dateTimeFormatter.getDate(jobreport.planned_delivery)
        holder?.iconText?.text = jobreport.customer_name.substring(0,1)
        applyProfilePicture(holder!!, jobreport)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.view_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return jobreports.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val customer_name = itemView.findViewById<TextView>(R.id.customer_name)
        val customer_address = itemView.findViewById<TextView>(R.id.customer_address)
        val customer_phone = itemView.findViewById<TextView>(R.id.customer_phone)
        val timestamp = itemView.findViewById<TextView>(R.id.timestamp) as TextView
        val datestamp = itemView.findViewById<TextView>(R.id.datestamp) as TextView
        val messageContainer = itemView.findViewById<LinearLayout>(R.id.message_container)
        val iconText = itemView.findViewById<TextView>(R.id.icon_text)
        val iconContainer = itemView.findViewById<RelativeLayout>(R.id.icon_container)
        val iconBack = itemView.findViewById<RelativeLayout>(R.id.icon_back)
        val iconFront = itemView.findViewById<RelativeLayout>(R.id.icon_front)
        val imgProfile = itemView.findViewById<ImageView>(R.id.icon_profile)
        val viewBack = itemView.findViewById<RelativeLayout>(R.id.view_background)
        val viewFront = itemView.findViewById<RelativeLayout>(R.id.view_foreground)
    }

    private fun applyProfilePicture(holder: ViewHolder, jobReport: JobReport) {
        holder.imgProfile.setImageResource(R.drawable.bg_circle)
        holder.imgProfile.setColorFilter(Color.RED)
        holder.iconText.setVisibility(View.VISIBLE)
    }
}