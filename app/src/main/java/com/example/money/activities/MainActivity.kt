package com.example.money.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.money.R
import com.example.money.databinding.ActivityMainBinding
import com.example.money.model.Money
import com.example.money.util.DateTimeUtil
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private val dateTimeUtil = DateTimeUtil()

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        Thread.sleep(1500)
        val splashScreen = installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.dateTextView.text = dateTimeUtil.getDateString()

        val currentHour = dateTimeUtil.getHour()
        if (currentHour < 9) {
            binding.greetingTextView.text = getString(R.string.greeting_morning)
        } else if (currentHour < 18) {
            binding.greetingTextView.text = getString(R.string.greeting_afternoon)
        } else {
            binding.greetingTextView.text = getString(R.string.greeting_night)
        }
        setGoalLabel()

        binding.tabImageButton.setOnClickListener {

            val s = Intent(this@MainActivity, com.example.money.activities.ExchangeActivity::class.java)
            startActivity(s)
        }
    }
    fun getMonth(): String {
        val sdf = SimpleDateFormat("MM")
        val currentDate = sdf.format(Date())
        return when (currentDate.toInt()) {
            1 -> getString(R.string.in_month_january)
            2 -> getString(R.string.in_month_february)
            3 -> getString(R.string.in_month_march)
            4 -> getString(R.string.in_month_april)
            5 -> getString(R.string.in_month_may)
            6 -> getString(R.string.in_month_june)
            7 -> getString(R.string.in_month_july)
            8 -> getString(R.string.in_month_august)
            9 -> getString(R.string.in_month_september)
            10 -> getString(R.string.in_month_october)
            11 -> getString(R.string.in_month_november)
            12 -> getString(R.string.in_month_december)
            else -> {
                ""
            }
        }
    }
    fun setGoalLabel() {
        if (Money.getGoal() == null || Money.getGoal() == 0) {
            binding.goalTextView.text = getString(R.string.set_goal_warning)
        } else {

            if (Money.getSpendableMoney() == 0) {
                binding.goalTextView.text = getString(R.string.limit_reached_warning)
            } else if (Money.getSpendableMoney() < 0) {
                binding.goalTextView.text = getString(R.string.limit_exceeded_warning)
            } else {
                binding.goalTextView.text = getMonth() + " még ennyit költhetsz:\n" +
                        Money.getGoal() + " " + Money.getCurrency()
            }
        }
    }
}