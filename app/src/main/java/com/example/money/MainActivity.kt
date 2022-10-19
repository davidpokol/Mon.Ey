package com.example.money

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.example.money.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSupportActionBar()?.hide()

        binding.dateTextView.text = getDate()

    }

    @SuppressLint("SimpleDateFormat")
    private fun getDate(): String {

        val sdf = SimpleDateFormat("yyyy.MM.dd")
        val currentDate = sdf.format(Date())

        return currentDate;
    }
}