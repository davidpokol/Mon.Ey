package com.example.money.activity

import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.money.R
import com.example.money.db.MyDBHelper
import com.example.money.model.Purchase
import com.example.money.util.DateTimeUtil
import com.example.money.util.StringUtil
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry


private val dateTimeUtil = DateTimeUtil()
private val stringUtil = StringUtil()

private lateinit var myDBHelper: MyDBHelper
private lateinit var myDB: SQLiteDatabase
private lateinit var settingsRs: Cursor
private lateinit var rs: Cursor

private lateinit var barEntriesList: ArrayList<BarEntry>
private lateinit var barDataSet: BarDataSet
private lateinit var barChart: BarChart
private lateinit var spinner: Spinner
private lateinit var listView: ListView

private var currency = "forint"


class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        supportActionBar?.hide()

        barChart = findViewById(R.id.barchart)
        spinner = findViewById(R.id.spinner)
        listView = findViewById(R.id.listView)

        myDBHelper = MyDBHelper(this)
        myDB = myDBHelper.readableDatabase

        val purchases = getPurchases()

        settingsRs = myDB.rawQuery("SELECT currency_index FROM settings", null)
        if (purchases.isEmpty()) {
            return
        }

        getCurrencyFromDataBase()

        spinner.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item,
            getSpinnerValues(purchases)
        )

        spinner.setSelection(0)
        setupBarChar(purchases)


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                setupBarChar(purchases)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }
    }
    override fun onBackPressed() {
        settingsRs.close()
        rs.close()
        myDB.close()
        super.onBackPressed()
        this.finish()
    }

    private fun getCurrencyFromDataBase() {
        if (settingsRs.moveToFirst()) {
            currency = resources.getStringArray(R.array.currencies)[settingsRs.getInt(0)]
                .toString().toLowerCase()
            settingsRs.close()
        }
    }
    private fun getPurchases(): List<Purchase> {

        rs = myDB.rawQuery("SELECT date, amount, category FROM purchases", null)
        val purchases = mutableListOf<Purchase>()

        if (rs.moveToFirst()) {
            do {
                val purchase = Purchase()
                purchase.purchaseDate = dateTimeUtil.stringToDate(rs.getString(0))
                purchase.amount = rs.getInt(1)
                purchase.categoryIndex = rs.getInt(2)
                purchases.add(purchase)

            } while (rs.moveToNext())
        } else {
            barChart.isVisible = false
            spinner.isVisible = false
            val alert = AlertDialog.Builder(this)
            alert.setMessage(getString(R.string.no_added_purchase))
            alert.setPositiveButton(getString(R.string.positive_button_okay),
                DialogInterface.OnClickListener { _, _ ->
                    this.finish()
                })
            alert.setOnDismissListener {
                onBackPressed()
            }
            alert.show()
        }
        return purchases
    }

    private fun getSpinnerValues(purchases: List<Purchase>): List<String> {

        val result = mutableListOf<String>()

        for (purchase in purchases) {
            val current = dateTimeUtil.getMonth(purchase.purchaseDate)

            if (!result.contains(current)) {
                result.add(current)
            }
        }
        return result
    }

    private fun getMonthPurchases(purchases: List<Purchase>, month: String): List<Purchase> {
        val result = mutableListOf<Purchase>()

        for (purchase in purchases) {

            if(dateTimeUtil.getMonth(purchase.purchaseDate) == month) {
                result.add(purchase)
            }
        }

        return result
    }

    private fun setupBarChar(purchases: List<Purchase>) {

        barEntriesList = arrayListOf<BarEntry>()
        val monthPurchases = getMonthPurchases(
            purchases,
            spinner.selectedItem.toString()
        )

        val adapterList = mutableListOf<String>()
        for(i in 0 until resources.getStringArray(R.array.categories).size) {
            var sum = 0
            for(j in monthPurchases) {

                if(j.categoryIndex == i) {
                    sum += j.amount
                }
            }
            if(sum == 0) {
                barEntriesList.add(BarEntry((i + 1).toFloat() , sum.toFloat()
                ))
            } else {
                barEntriesList.add(BarEntry((i + 1).toFloat() , sum.toFloat(),
                    getDrawable(getIcon(i))
                ))
                adapterList.add(resources.getStringArray(R.array.categories)[i] +
                        ": "+ stringUtil.formatAmount(sum.toString()) +
                        " " + currency)
            }

        }

        listView.adapter = ArrayAdapter(
            applicationContext,
            R.layout.list_item, R.id.text_view, adapterList

        )

        barDataSet = BarDataSet(barEntriesList, spinner.selectedItem.toString())
        val data = BarData(barDataSet)
        data.barWidth = 0.9f
        barDataSet.color = getColor(R.color.money_dark_green)
        barDataSet.setDrawValues(false)
        barChart.data = data

        barChart.setFitBars(true)
        barChart.invalidate()
        barChart.animateY(500)
        barChart.xAxis.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.description.text = ""
    }

    private fun getIcon(index : Int) : Int {
        return when (index) {
            0 -> R.drawable.bills
            1 -> R.drawable.restaurant
            2 -> R.drawable.entertainment
            3 -> R.drawable.donation
            4 -> R.drawable.groceries
            5 -> R.drawable.healthwellness
            6 -> R.drawable.transportation
            7 -> R.drawable.homeacc
            8 -> R.drawable.clothing
            9 -> R.drawable.travel
            10 -> R.drawable.other
            else -> {
                R.drawable.other
            }
        }

    }
}