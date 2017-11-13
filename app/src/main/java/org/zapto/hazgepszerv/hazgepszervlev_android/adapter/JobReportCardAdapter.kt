package org.zapto.hazgepszerv.hazgepszervlev_android.adapter

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat.getColor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.zapto.hazgepszerv.hazgepszervlev_android.R
import org.zapto.hazgepszerv.hazgepszervlev_android.model.JobReport
import org.zapto.hazgepszerv.hazgepszervlev_android.utils.DateTimeFormatter
import java.util.ArrayList

class JobReportCardAdapter(val jobreports : ArrayList<JobReport>, val context: Context): RecyclerView.Adapter<JobReportCardAdapter.ViewHolder>(){

    val formatter = DateTimeFormatter()

    override fun getItemCount(): Int {
        return jobreports.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val jobreport : JobReport = jobreports[position]

        holder?.customer_name?.text = jobreport.customer_name
        holder?.customer_address?.text = jobreport.customer_address
        holder?.planned_delivery?.text = formatter.getTime(jobreport.planned_delivery)
        holder?.problem_review?.text = jobreport.problem_review
        holder?.designButton?.setBackgroundColor(getRandomMaterialColor("400"))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.view_item_card, parent, false)
        return JobReportCardAdapter.ViewHolder(v)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val customer_name = itemView.findViewById<TextView>(R.id.card_customer_name)
        val customer_address = itemView.findViewById<TextView>(R.id.card_customer_address)
        val planned_delivery = itemView.findViewById<TextView>(R.id.card_planned_delivery)
        val problem_review = itemView.findViewById<TextView>(R.id.card_problem)
        val designButton = itemView.findViewById<TextView>(R.id.button2)
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