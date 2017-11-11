package org.zapto.hazgepszerv.hazgepszervlev_android.fragments

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.app_bar_main.*
import org.zapto.hazgepszerv.hazgepszervlev_android.activities.MainActivity
import org.zapto.hazgepszerv.hazgepszervlev_android.R
import android.app.DatePickerDialog
import java.util.*
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import br.com.sapereaude.maskedEditText.MaskedEditText
import org.zapto.hazgepszerv.hazgepszervlev_android.model.JobReport


class NewJobReportFragment : Fragment(), View.OnClickListener {
    lateinit var layout: ConstraintLayout
    lateinit var ugyfel_neve: EditText
    lateinit var ugyfel_cime:EditText
    lateinit var ugyfel_szama:MaskedEditText
    lateinit var tervezett_kiszallas:EditText
    lateinit var hibajelenseg:EditText
    lateinit var hozzaad: Button
    var time : String? = null

    lateinit var date_time : String

    override fun onClick(p0: View?) {
        if (p0?.equals(tervezett_kiszallas)!!)
            datePicker()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.add_new_jobreport, container, false)

        val mainActivity = activity as MainActivity
        mainActivity.toolbar.title = "Új hibajegy"
        //mainActivity.fab.hide()

        mainActivity.fab.visibility = View.GONE

        ugyfel_neve = view?.findViewById<View>(R.id.ugyfel_neve) as EditText
        ugyfel_cime = view.findViewById<View>(R.id.ugyfel_cime) as EditText
        ugyfel_szama = view.findViewById<View>(R.id.ugyfel_telefonszama) as MaskedEditText
        tervezett_kiszallas = view.findViewById<View>(R.id.tervezett_kiszallas) as EditText
        hibajelenseg = view.findViewById<View>(R.id.hibajelenseg) as EditText
        hozzaad = view.findViewById<Button>(R.id.button_add_report)

        if (time != " "){
            tervezett_kiszallas.setText(time)
        }

        hozzaad.setOnClickListener { addReport() }

        /*val slidr = view.findViewById<Slidr>(R.id.munkahossza)
        slidr.max = 120f
        slidr.setTextMin("perc")
        slidr.setTextMax("perc")
        slidr.setTextFormatter({ value -> value.toInt().toString() })*/

        tervezett_kiszallas.setOnClickListener(this)

        return view
    }

    private fun datePicker() {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        var mMonth = c.get(Calendar.MONTH)
        var mDay = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(context,
                OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    var formattedMonth : String = (monthOfYear+1).toString()
                    if(monthOfYear < 10){
                        formattedMonth = "0" + (monthOfYear + 1)
                    }
                    var formattedDayOfMonth : String = dayOfMonth.toString()
                    if(dayOfMonth < 10){
                        formattedDayOfMonth = "0" + dayOfMonth
                    }
                    date_time = year.toString() + "-" + formattedMonth + "-" + formattedDayOfMonth
                    timePicker()
                }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    private fun timePicker() {
        val c = Calendar.getInstance()
        var mHour = c.get(Calendar.HOUR_OF_DAY)
        var mMinute = c.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(context,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    mHour = hourOfDay
                    mMinute = minute

                    var formattedHour : String = mHour.toString()
                    if(mHour < 10){
                        formattedHour = "0" + mHour
                    }
                    var formattedMinute : String = mMinute.toString()
                    if(mMinute < 10){
                        formattedMinute = "0" + mMinute
                    }
                    tervezett_kiszallas.setText(date_time + " " + formattedHour + ":" + formattedMinute)
                }, mHour, mMinute, false)
        timePickerDialog.show()
    }

    private fun addReport() {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("jobreports")

        val newItem = reference.push()

        val jobreport = JobReport(ugyfel_neve.text.toString(),
                                              ugyfel_cime.text.toString(),
                                              ugyfel_szama.text.toString(),
                                              tervezett_kiszallas.text.toString(),
                                              hibajelenseg.text.toString(),
                                              "open",
                                              newItem.key)

        newItem.setValue(jobreport)

        var asd: Fragment? = null

        try {
            asd = TabbedJobreportsFragment::class.java!!.newInstance()
        } catch (e: java.lang.InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        val fragmentManager = activity.supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.flContent, asd).commit()
    }
}