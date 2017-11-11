package org.zapto.hazgepszerv.hazgepszervlev_android.utils

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateTimeFormatter {

    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")

    fun getFullDate(delivery: String) : Date {
        var date = Date()
        try {
            date = sdf.parse(delivery)
        } catch (e: ParseException){
            Log.d("JobReport", e.printStackTrace().toString())
        }
        return date
    }

    fun getTime(delivery : String) : String {
        var time = " "
        try {
            val timeSdf = SimpleDateFormat("HH:mm")
            val temp : Date = sdf.parse(delivery)
            time = timeSdf.format(temp)
        }
        catch (e: ParseException) {
            Log.d("JobReport", e.printStackTrace().toString())
        }
        return time
    }

    fun getDate(delivery: String) : String {
        var date = " "
        try {
            val dateSdf = SimpleDateFormat("MMM dd", Locale("hu"))
            val temp: Date = sdf.parse(delivery)
            date = dateSdf.format(temp)
        } catch (e: ParseException){
            Log.d("JobReport", e.printStackTrace().toString())
        }
        return date
    }
}