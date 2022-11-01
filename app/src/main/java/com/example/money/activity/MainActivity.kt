package com.example.money.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.money.R
import com.example.money.databinding.ActivityMainBinding
import com.example.money.enums.Currency
import com.example.money.model.Money
import com.example.money.model.Purchase
import com.example.money.model.Purchases
import com.example.money.util.DateTimeUtil
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@SuppressLint("SimpleDateFormat")
class MainActivity : AppCompatActivity() {

    private val dateTimeUtil = DateTimeUtil()
    private val money = Money()
    private val purchases = Purchases()

    private lateinit var binding: ActivityMainBinding
    private lateinit var pieEntriesList: ArrayList<PieEntry>
    private lateinit var pieDataSet: PieDataSet

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        Thread.sleep(1500)
        installSplashScreen()
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
        setUpPieChart()

        binding.tabImageButton.setOnClickListener {

            val s = Intent(this@MainActivity, SettingsActivity::class.java)
            this.startActivity(s)
        }

        binding.tabImageButton2.setOnClickListener {

            val s = Intent(this@MainActivity, PurchaseActivity::class.java)
            this.startActivity(s)
        }
    }

    public override fun onResume() {
        super.onResume()
        setGoalLabel()
        setUpPieChart()
    }

    private fun setGoalLabel() {
        if (money.goal == 0) {
            binding.goalTextView.text = getString(R.string.set_goal_warning)
            return
        }
        if (getSpendableMoney() == 0) {
            binding.goalTextView.text = getString(R.string.limit_reached_warning)
            return
        }
        if (getSpendableMoney() < 0) {
            binding.goalTextView.text = getString(R.string.limit_exceeded_warning)
            return
        }

        binding.goalTextView.text = String.format(getString(R.string.goal_info_message),
            getMonth(),
            getSpendableMoney(),
            getCurrencyText(money.currency))

    }

    private fun setUpPieChart() {
        if(money.goal != null) {
            val pieChart: PieChart = findViewById(R.id.spendsPieChart)
            pieEntriesList = ArrayList<PieEntry>()
            pieEntriesList.add(PieEntry(getThisMonthMoneySpent().toFloat()))
            pieEntriesList.add(PieEntry(getSpendableMoney().toFloat()))
            pieDataSet = PieDataSet(pieEntriesList, "")
            pieDataSet.setColors(
                intArrayOf(
                    Color.rgb(200, 200, 200),
                    Color.rgb(0, 195, 110)
                ), 255
            )
            pieDataSet.setDrawValues(false)
            pieChart.legend.isEnabled = false
            pieChart.data = PieData(pieDataSet)
            pieChart.animateXY(500, 500)
            pieChart.description.text = "";
        }
    }
    private fun getMonth(): String {

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

    private fun getCurrencyText(c: Currency) : String {

        if(c == Currency.FORINT){
            return getString(R.string.forint)
        }
        if(c == Currency.EURO){
            return getString(R.string.euro)
        }
        return getString(R.string.dollar)
    }

    private fun getThisMonthMoneySpent() : Int {
        var sum : Int = 0
        for(p : Purchase in purchases.thisMonthPurchases) {
            sum += p.amount
        }
        return sum
    }
    private fun getSpendableMoney() : Int {
        return money.goal - getThisMonthMoneySpent()
    }
}