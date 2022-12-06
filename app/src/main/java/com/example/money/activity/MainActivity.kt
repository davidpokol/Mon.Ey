package com.example.money.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import com.example.money.R
import com.example.money.databinding.ActivityMainBinding
import com.example.money.db.MyDBHelper
import com.example.money.model.Settings
import com.example.money.model.Purchase
import com.example.money.util.DateTimeUtil
import com.example.money.util.PurchaseUtil
import com.example.money.util.StringUtil
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@SuppressLint("Recycle","SimpleDateFormat")
class MainActivity : AppCompatActivity() {

    private val dateTimeUtil = DateTimeUtil()
    private val settings = Settings()
    private val stringUtil = StringUtil()
    private val purchaseUtil = PurchaseUtil()

    private lateinit var binding: ActivityMainBinding
    private lateinit var pieEntriesList: ArrayList<PieEntry>
    private lateinit var pieDataSet: PieDataSet

    private lateinit var myDBHelper: MyDBHelper
    private lateinit var settingsMyDB: SQLiteDatabase
    private lateinit var purchasesMyDB: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myDBHelper = MyDBHelper(this)
        Thread.sleep(1500)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.dateTextView.text = dateTimeUtil.dateToString(Date())

        val currentHour = dateTimeUtil.getHour()
        if (currentHour < 9) {
            binding.greetingTextView.text = getString(R.string.greeting_morning)
        } else if (currentHour < 18) {
            binding.greetingTextView.text = getString(R.string.greeting_afternoon)
        } else {
            binding.greetingTextView.text = getString(R.string.greeting_night)
        }

        val purchases = getPurchasesToListFromDatabase()
        val thisMonthMoneySpent = getThisMonthMoneySpent(purchases)
        getLimitLabelDataFromDataBase()
        setLimitLabel(settings.monthLimit - thisMonthMoneySpent)
        setUpPieChart(thisMonthMoneySpent,settings.monthLimit - thisMonthMoneySpent)


        binding.limitTextView.setOnLongClickListener {
            val s = Intent(this@MainActivity, SettingsActivity::class.java)
            this.startActivity(s)
            return@setOnLongClickListener true
        }

        popupMenu()
    }

    public override fun onResume() {
        super.onResume()
        val purchases = getPurchasesToListFromDatabase()
        val thisMonthMoneySpent = getThisMonthMoneySpent(purchases)
        getLimitLabelDataFromDataBase()
        setLimitLabel(settings.monthLimit - thisMonthMoneySpent)
        setUpPieChart(thisMonthMoneySpent,settings.monthLimit - thisMonthMoneySpent)
    }

    private fun getPurchasesToListFromDatabase() : List<Purchase> {
        purchasesMyDB = myDBHelper.readableDatabase

        val list = mutableListOf<Purchase>()
        val rs = purchasesMyDB.rawQuery("SELECT * FROM purchases", null)
        if (rs.moveToFirst()) {
            do {
                val purchase = Purchase()
                purchase.id = rs.getInt(0)
                purchase.purchaseDate = dateTimeUtil.stringToDate(rs.getString(1))
                purchase.place = rs.getString(2)
                purchase.amount = rs.getInt(3)
                purchase.categoryIndex = rs.getInt(4)
                list.add(purchase)

                println("a:" + purchase.amount)

            } while (rs.moveToNext())
        }
        return list
    }

    private fun setLimitLabel(spendableMoney : Int) {
        if (settings.monthLimit == 0) {
            binding.limitTextView.text = getString(R.string.set_goal_warning)
            return
        }
        if (spendableMoney == 0) {
            binding.limitTextView.text = getString(R.string.limit_reached_warning)
            return
        }
        if (spendableMoney < 0) {
            binding.limitTextView.text = getString(R.string.limit_exceeded_warning)
            return
        }

        binding.limitTextView.text = String.format(
            getString(R.string.goal_info_message),
            getMonth(),
            stringUtil.formatAmount(spendableMoney.toString()),
            getCurrencyText()
        )
    }

    private fun getLimitLabelDataFromDataBase() {
        settingsMyDB = myDBHelper.readableDatabase

        val rs = settingsMyDB.rawQuery(
            "SELECT month_limit, currency_index " +
                    "FROM settings", null
        )

        if (rs.moveToFirst()) {
            settings.monthLimit = rs.getInt(0)
            settings.currencyIndex = rs.getInt(1)
        } else {
            Toast.makeText(
                this,
                getString(R.string.database_error_toast), Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun setUpPieChart(spent: Int, spendable: Int) {

        val pieChart: PieChart = findViewById(R.id.spendsPieChart)
        if (settings.monthLimit != 0 && spendable > 0 && settings.monthLimit != null) {
            pieChart.isVisible = true
            pieEntriesList = ArrayList()
                pieEntriesList.add(PieEntry(spent.toFloat()))
                pieEntriesList.add(PieEntry(spendable.toFloat()))
                pieDataSet = PieDataSet(pieEntriesList, "")
                pieDataSet.setColors(
                    intArrayOf(
                        Color.rgb(200, 200, 200), //GREY
                        Color.rgb(0, 195, 110) // GREEN
                    ), 255
                )

            pieDataSet.setDrawValues(false)
            pieChart.legend.isEnabled = false
            pieChart.data = PieData(pieDataSet)
            pieChart.animateXY(500, 500)
            pieChart.description.text = ""
        } else {
            pieChart.isVisible = false
        }

    }

    @SuppressLint("DiscouragedPrivateApi")
    private fun popupMenu() {
        // creating a object of Popupmenu
        val popupMenu = PopupMenu(this, binding.tabImageButton)

        // we need to inflate the object
        // with popup_menu.xml file
        popupMenu.inflate(R.menu.popup_menu)

        // adding click listener to image
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings_menu -> {
                    val s = Intent(this@MainActivity, SettingsActivity::class.java)
                    this.startActivity(s)
                    true
                }
                R.id.add_purchase_menu -> {
                    val s = Intent(this@MainActivity, PurchaseActivity::class.java)
                    this.startActivity(s)
                    true
                }
                R.id.list_purchases_menu -> {
                    val s = Intent(this@MainActivity, ListActivity::class.java)
                    this.startActivity(s)
                    true
                }
                R.id.categorization_menu -> {
                    val s = Intent(this@MainActivity, CategoryActivity::class.java)
                    this.startActivity(s)
                    true
                }
                R.id.about_menu -> {
                    val s = Intent(this@MainActivity, AboutActivity::class.java)
                    this.startActivity(s)
                    true
                }
                else -> {
                    true
                }
            }
        }

        binding.tabImageButton.setOnClickListener {
            try {
                val popup = PopupMenu::class.java.getDeclaredField("mPopup")
                popup.isAccessible = true
                val menu = popup.get(popupMenu)
                menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(menu,true)
            }
            catch (e: Exception)
            {
                Log.d("error", e.toString())
            }
            finally {
                popupMenu.show()
            }
            true

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

    private fun getCurrencyText(): String {
        return resources.getStringArray(R.array.can_spend_currencies)[settings.currencyIndex]
    }

    private fun getThisMonthMoneySpent(purchases: List<Purchase>): Int {
        var sum = 0
        val thisMonthPurchases = purchaseUtil.getThisMonthPurchases(purchases)
        for (purchase in thisMonthPurchases) {
            sum += purchase.amount
        }
        return sum
    }
}