package com.example.money.util

import android.annotation.SuppressLint
import android.content.res.Resources
import com.example.money.R
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class DateTimeUtil {
    fun getDateString(): String {

        val sdf = SimpleDateFormat("yyyy. MM. dd.")
        return sdf.format(Date())
    }

    fun getHour(): Int {

        val sdf = SimpleDateFormat("HH")
        val currentDate = sdf.format(Date())
        return currentDate.toInt()
    }

    fun getMonth(): String {
        val sdf = SimpleDateFormat("MM")
        val currentDate = sdf.format(Date())
        return when (currentDate.toInt()) {
            1 -> Resources.getSystem().getString(R.string.in_month_january)
            2 -> Resources.getSystem().getString(R.string.in_month_february)
            3 -> Resources.getSystem().getString(R.string.in_month_march)
            4 -> Resources.getSystem().getString(R.string.in_month_april)
            5 -> Resources.getSystem().getString(R.string.in_month_may)
            6 -> Resources.getSystem().getString(R.string.in_month_june)
            7 -> Resources.getSystem().getString(R.string.in_month_july)
            8 -> Resources.getSystem().getString(R.string.in_month_august)
            9 -> Resources.getSystem().getString(R.string.in_month_september)
            10 -> Resources.getSystem().getString(R.string.in_month_october)
            11 -> Resources.getSystem().getString(R.string.in_month_november)
            12 -> Resources.getSystem().getString(R.string.in_month_december)
            else -> {
                ""
            }
        }
    }
}