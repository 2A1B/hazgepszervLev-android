package org.zapto.hazgepszerv.hazgepszervlev_android.adapter

import android.content.Context
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

class JobReportAdapter(val jobreports : ArrayList<JobReport>, val context : Context) : RecyclerView.Adapter<JobReportAdapter.ViewHolder>() {

    private val dateTimeFormatter : DateTimeFormatter = DateTimeFormatter()

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val jobreport : JobReport = jobreports[position]

        holder?.customer_name?.text = jobreport.customer_name
        holder?.customer_address?.text = jobreport.customer_address
        holder?.customer_phone?.text = jobreport.customer_phone
        holder?.timestamp?.text = dateTimeFormatter.getTime(jobreport.planned_delivery)
        holder?.datestamp?.text = dateTimeFormatter.getDate(jobreport.planned_delivery)
        applyProfilePicture(holder!!, jobreport.job_title)
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
        val iconContainer = itemView.findViewById<RelativeLayout>(R.id.icon_container)
        val imgProfile = itemView.findViewById<ImageView>(R.id.icon_profile)
        val imgProfileBackGround = itemView.findViewById<ImageView>(R.id.icon_profile_background)
        val viewFront = itemView.findViewById<RelativeLayout>(R.id.view_foreground)
    }

    private fun applyProfilePicture(holder: ViewHolder, title: String) {
        if(title.equals("szerviz")) {
            holder.imgProfile.setImageResource(R.drawable.screwdriver)
            holder.imgProfileBackGround.setImageResource(R.drawable.bg_circle)
            holder.imgProfileBackGround.setColorFilter(getRandomMaterialColor("400"))
        } else if (title.equals("felmérés")) {
            holder.imgProfile.setImageResource(R.drawable.in_transit)
            holder.imgProfileBackGround.setImageResource(R.drawable.bg_circle)
            holder.imgProfileBackGround.setColorFilter(getRandomMaterialColor("400"))
        } else if (title.equals("más")) {
            holder.imgProfile.setImageResource(R.drawable.swiss_army_knife)
            holder.imgProfileBackGround.setImageResource(R.drawable.bg_circle)
            holder.imgProfileBackGround.setColorFilter(getRandomMaterialColor("400"))
        } else if (title.equals("helyszíni")) {
            holder.imgProfile.setImageResource(R.drawable.home_service)
            holder.imgProfileBackGround.setImageResource(R.drawable.bg_circle)
            holder.imgProfileBackGround.setColorFilter(getRandomMaterialColor("400"))
        }
    }

    private fun getRandomMaterialColor(typeColor: String): Int {
        var returnColor = Color.GRAY
        val arrayId = context.getResources().getIdentifier("mdcolor_" + typeColor, "array", context.getPackageName())

        if (arrayId != 0) {
            val colors = context.getResources().obtainTypedArray(arrayId)
            val index = (Math.random() * colors.length()).toInt()
            returnColor = colors.getColor(index, Color.GRAY)
            colors.recycle()
        }
        return returnColor
    }
}