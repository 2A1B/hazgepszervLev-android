package org.zapto.hazgepszerv.hazgepszervlev_android.fragments

import android.app.Dialog
import android.graphics.RectF
import android.os.Bundle
import android.support.v4.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.app_bar_main.*
import org.zapto.hazgepszerv.hazgepszervlev_android.R
import org.zapto.hazgepszerv.hazgepszervlev_android.activities.MainActivity
import org.zapto.hazgepszerv.hazgepszervlev_android.model.JobReport
import org.zapto.hazgepszerv.hazgepszervlev_android.utils.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*
import android.util.TypedValue
import android.view.*
import android.view.MenuInflater
import com.alamkanak.weekview.*
import com.alamkanak.weekview.WeekViewEvent
import kotlinx.coroutines.experimental.Job
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton

abstract class BaseCalendarFragment : Fragment(), WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener{

    private val TYPE_DAY_VIEW = 1
    private val TYPE_THREE_DAY_VIEW = 2
    private val TYPE_WEEK_VIEW = 3
    private var mWeekViewType = TYPE_THREE_DAY_VIEW

    lateinit var calendar : WeekView

    val events = ArrayList<WeekViewEvent>()
    val jobreports = ArrayList<JobReport>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.calendar_fragment, container, false)

        val mainActivity = activity as MainActivity
        mainActivity.toolbar.title = "Naptár"
        mainActivity.fab.visibility = View.GONE

        calendar = rootView.findViewById<WeekView>(R.id.weekView)

        calendar.setOnEventClickListener(this)
        calendar.monthChangeListener = this
        calendar.eventLongPressListener = this
        calendar.emptyViewLongPressListener = this

        setHasOptionsMenu(true)

        return rootView
    }

    override fun onEventClick(event: WeekViewEvent?, eventRect: RectF?) {
        val position = event?.id

        val address = "Cím: " + jobreports.get(position?.toInt()!!).customer_address
        val phone = "Telefonszám: " + jobreports.get(position.toInt()).customer_phone
        val problem = "Probléma: " + jobreports.get(position.toInt()).problem_review

        val message = address + "\n" + phone + "\n" + problem

        alert(message, jobreports.get(position.toInt()).customer_name) {
            yesButton {}
        }.show()

    }

    override fun onEventLongPress(event: WeekViewEvent?, eventRect: RectF?) {
    }

    override fun onEmptyViewLongPress(time: Calendar?) {
        val date : Date = time?.time!!
        val format1 = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val date1 = format1.format(date)
        val asd = NewJobReportFragment::class.java.newInstance()
        asd.time = date1
        val fragmentManager = fragmentManager
        fragmentManager.beginTransaction().replace(R.id.flContent, asd).addToBackStack(asd.toString()).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.calendar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        setupDateTimeInterpreter(id == R.id.action_week_view)
        when (id) {
            R.id.action_today -> {
                calendar.goToToday()
                return true
            }
            R.id.action_day_view -> {
                if (mWeekViewType !== TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked())
                    mWeekViewType = TYPE_DAY_VIEW
                    calendar.setNumberOfVisibleDays(1)
                    calendar.setColumnGap(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics).toInt())
                    calendar.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics).toInt())
                    calendar.setEventTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics).toInt())
                }
                return true
            }
            R.id.action_three_day_view -> {
                if (mWeekViewType !== TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked())
                    mWeekViewType = TYPE_THREE_DAY_VIEW
                    calendar.setNumberOfVisibleDays(3)
                    calendar.setColumnGap(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics).toInt())
                    calendar.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics).toInt())
                    calendar.setEventTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics).toInt())
                }
                return true
            }
            R.id.action_week_view -> {
                if (mWeekViewType !== TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked())
                    mWeekViewType = TYPE_WEEK_VIEW
                    calendar.setNumberOfVisibleDays(7)
                    calendar.setColumnGap(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, resources.displayMetrics).toInt())
                    calendar.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10f, resources.displayMetrics).toInt())
                    calendar.setEventTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10f, resources.displayMetrics).toInt())
                }
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    protected fun getEventTitle(time: Calendar, name: String): String {
        return String.format("%s %02d:%02d %s/%d", name, time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH))
    }

    private fun setupDateTimeInterpreter(shortDate: Boolean) {
        calendar.dateTimeInterpreter = object : DateTimeInterpreter {
            override fun interpretDate(date: Calendar): String {
                val weekdayNameFormat = SimpleDateFormat("EEE", Locale.getDefault())
                var weekday = weekdayNameFormat.format(date.time)
                val format = SimpleDateFormat(" M/d", Locale.getDefault())

                if (shortDate)
                    weekday = weekday[0].toString()
                return weekday.toUpperCase() + format.format(date.time)
            }

            override fun interpretTime(hour: Int): String {
                return if (hour > 11) (hour - 12).toString() + " PM" else if (hour == 0) "12 AM" else hour.toString() + " AM"
            }
        }
    }
}