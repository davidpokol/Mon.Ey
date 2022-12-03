package com.example.money.util

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class DateTimeUtil : AppCompatActivity(){

    private val dateFormatter = SimpleDateFormat("yyyy. MM. dd.")
    private val monthFormatter = SimpleDateFormat("yyyy. MM.")
    private val hourFormatter = SimpleDateFormat("HH")

    fun dateToString(date : Date): String {
        return dateFormatter.format(date)
    }

    fun stringToDate(string : String): Date {
        return dateFormatter.parse(string) as Date
    }

    fun getMonth(date : Date) : String {
        return monthFormatter.format(date)
    }

    fun getHour(): Int {
        return hourFormatter.format(Date()).toInt()
    }
}