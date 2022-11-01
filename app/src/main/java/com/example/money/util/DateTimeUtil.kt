package com.example.money.util

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.money.R
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class DateTimeUtil : AppCompatActivity(){

    fun getDateString(): String {

        val sdf = SimpleDateFormat("yyyy. MM. dd.")
        return sdf.format(Date())
    }

    fun getHour(): Int {

        val sdf = SimpleDateFormat("HH")
        val currentDate = sdf.format(Date())
        return currentDate.toInt()
    }
}