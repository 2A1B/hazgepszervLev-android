package org.zapto.hazgepszerv.hazgepszervlev_android.fragments

import com.alamkanak.weekview.WeekViewEvent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.zapto.hazgepszerv.hazgepszervlev_android.R
import org.zapto.hazgepszerv.hazgepszervlev_android.model.JobReport
import org.zapto.hazgepszerv.hazgepszervlev_android.utils.DateTimeFormatter
import java.util.*

class AsynchronousCalendar : BaseCalendarFragment(){

    private val dateTimeFormatter : DateTimeFormatter = DateTimeFormatter()

    fun readReports() {
        var startTime = Calendar.getInstance()
        var endTime = startTime.clone() as Calendar
        var weekViewEvent: WeekViewEvent

        val ref = FirebaseDatabase.getInstance().getReference("jobreports")

        var i : Long = 0

        ref.orderByChild("status").equalTo("open").addValueEventListener( object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(p0: DataSnapshot?) {
                events.clear()
                val children = p0!!.children
                children.forEach {
                    val jobreport = it.getValue(JobReport::class.java)
                    jobreports.add(jobreport!!)
                    val date = dateTimeFormatter.getFullDate(jobreport.planned_delivery)
                    startTime = Calendar.getInstance()
                    startTime.set(2017, date.month, date.date, date.hours, date.minutes)
                    endTime = Calendar.getInstance()
                    endTime.set(2017, date.month, date.date, date.hours+2, date.minutes)

                    weekViewEvent = WeekViewEvent(i, getEventTitle(startTime, jobreport.customer_name), startTime, endTime)
                    weekViewEvent.color = R.color.colorPrimary

                    events.add(weekViewEvent)

                    i++

                    calendar.notifyDatasetChanged()
                }
            }
        })
    }

    override fun onMonthChange(newYear: Int, newMonth: Int): MutableList<out WeekViewEvent> {
        val matchedEvents = ArrayList<WeekViewEvent>()
        readReports()
        for (event in events) {
            if (eventMatches(event, newYear, newMonth)) {
                matchedEvents.add(event)
            }
        }
        return matchedEvents
    }

    private fun eventMatches(event: WeekViewEvent, year: Int, month: Int): Boolean {
        return event.startTime.get(Calendar.YEAR) == year && event.startTime.get(Calendar.MONTH) == month - 1 || event.endTime.get(Calendar.YEAR) == year && event.endTime.get(Calendar.MONTH) == month - 1
    }
}
