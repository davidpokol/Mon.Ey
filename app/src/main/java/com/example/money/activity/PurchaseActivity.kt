package com.example.money.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.money.R
import com.example.money.enums.Category
import com.example.money.enums.Currency
import com.example.money.model.Money
import com.example.money.model.Purchase
import com.example.money.model.Purchases
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class PurchaseActivity : AppCompatActivity() {

    private val money = Money()
    private val purchases = Purchases()

    private val myCalendar = Calendar.getInstance()
    private lateinit var dateEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase)
        supportActionBar?.hide()

        dateEditText = findViewById(R.id.dateEditText)
        val purchasePlaceEditText : EditText = findViewById(R.id.purchasePlaceEditText)
        val amountTextView : EditText = findViewById(R.id.amountEditText)
        val currencySpinner : Spinner = findViewById(R.id.currencySpinner)
        val categorySpinner : Spinner = findViewById(R.id.categorySpinner)
        val submitButton : Button = findViewById(R.id.submitButton)
        var currencySpinnerValue : Currency = Currency.FORINT
        var categorySpinnerValue: Category = Category.OTHER

        val date =
            OnDateSetListener { view, year, month, day ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, day)
                updateLabel()
            }
        dateEditText.setOnClickListener(View.OnClickListener {
            DatePickerDialog(
                this@PurchaseActivity,
                date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        })



        currencySpinner.adapter = ArrayAdapter<Currency>(
            this, android.R.layout.simple_spinner_item, Currency.values())


        currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                currencySpinnerValue = currencySpinner.selectedItem as Currency
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        categorySpinner.adapter = ArrayAdapter<Category>(
            this, android.R.layout.simple_spinner_item, Category.values())


        currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                categorySpinnerValue = categorySpinner.selectedItem as Category
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        submitButton.setOnClickListener {

            var date : Date? = null
            var purchasePlace : String? = null
            var amount : Int = 0
            var isRightFill : Boolean = true
            try {
                date =  SimpleDateFormat("yyyy. MM. dd.").
                                                    parse(dateEditText.text.toString().trim())
            } catch (e : Exception) {
                Toast.makeText(applicationContext, getString(R.string.fill_date_field_warning),
                    Toast.LENGTH_SHORT).show()
                isRightFill = false
            }
            try {
                purchasePlace = purchasePlaceEditText.text.toString().trim()
                if(purchasePlace.isEmpty()){
                    throw Exception()
                }

            } catch (e: Exception) {
                Toast.makeText(applicationContext, getString(R.string.fill_place_field_warning),
                    Toast.LENGTH_SHORT).show()
                isRightFill = false
            }

            try {
                amount = amountTextView.text.toString().trim().toInt()

            } catch(e: Exception) {
                Toast.makeText(applicationContext, getString(R.string.fill_amount_field_warning),
                    Toast.LENGTH_SHORT).show()
                isRightFill = false

            }

            if(isRightFill) {
                addPurchaseToMonitorData(Purchase(
                    date,
                    purchasePlace,
                    amount,
                    currencySpinnerValue,
                    categorySpinnerValue))

                Toast.makeText(applicationContext, "Vársárlás hozzáadva!",
                    Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun addPurchaseToMonitorData(p : Purchase) {
        purchases.addPurchase(p)

        if(p.purchaseDate.month == Date().month) {
            purchases.addThisMonthPurchase(p)
        }
    }

    private fun refreshMonitorData() {
        for(p: Purchase in purchases.thisMonthPurchases) {
            if(p.purchaseDate.month != Date().month) {
                purchases.removePurchaseById(p.id)
            }
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    private fun updateLabel() {
        val myFormat = "yyyy. MM. dd."
        val dateFormat = SimpleDateFormat(myFormat)
        dateEditText.setText(dateFormat.format(myCalendar.time))
    }

}