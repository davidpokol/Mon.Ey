package com.example.money.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.money.R
import com.example.money.model.Purchase
import com.example.money.model.Purchases
import com.example.money.model.SpinnerValues
import com.example.money.util.StringUtil
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class PurchaseActivity : AppCompatActivity() {

    private val purchases = Purchases()
    private val stringUtil = StringUtil()
    private val myCalendar = Calendar.getInstance()

    private lateinit var dateEditText: EditText
    private lateinit var placeEditText: EditText
    private lateinit var amountTextView: EditText
    private lateinit var categorySpinner: Spinner

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase)
        supportActionBar?.hide()




        val submitButton: Button = findViewById(R.id.submitButton)
        dateEditText = findViewById(R.id.dateEditText)
        placeEditText = findViewById(R.id.purchasePlaceEditText)
        amountTextView = findViewById(R.id.amountEditText)
        categorySpinner = findViewById(R.id.categorySpinner)

        amountTextView.addTextChangedListener(stringUtil.onTextChangedListener(amountTextView))
        val date =
            OnDateSetListener { _, year, month, day ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, day)
                updateLabel()
            }
        dateEditText.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this@PurchaseActivity,
                date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate = 1640995261000 // 2022,01,01
            datePickerDialog.datePicker.maxDate = Date().time
            datePickerDialog.show()
        }

        categorySpinner.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.categories)
        )

        submitButton.setOnClickListener {

            val date: Date?
            val purchasePlace: String?
            val amount: Int
            try {
                date = SimpleDateFormat("yyyy. MM. dd.").parse(
                    dateEditText.text.toString().trim()
                )
            } catch (e: Exception) {
                Toast.makeText(
                    applicationContext, getString(R.string.fill_date_field_warning),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            try {
                purchasePlace = placeEditText.text.toString().trim()
                if (purchasePlace.isEmpty()) {
                    throw Exception()
                }

            } catch (e: Exception) {
                Toast.makeText(
                    applicationContext, getString(R.string.fill_place_field_warning),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            try {
                amount = stringUtil.deFormatAmount(amountTextView.text.toString())

            } catch (e: Exception) {
                Toast.makeText(
                    applicationContext, getString(R.string.fill_amount_field_warning),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener

            }

            addPurchaseToMonitorData(
                Purchase(
                    date,
                    purchasePlace,
                    amount,
                    categorySpinner.selectedItemPosition
                )
            )

            submitButton.isSoundEffectsEnabled = false
            MediaPlayer.create(this, R.raw.cash_machine).start()
            submitButton.isSoundEffectsEnabled = true
            Toast.makeText(
                applicationContext, getString(R.string.purchase_added),
                Toast.LENGTH_LONG
            ).show()
            resetInputFields()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }


    private fun addPurchaseToMonitorData(p: Purchase) {
        purchases.addPurchase(p)

        if (p.purchaseDate.month == Date().month) {
            purchases.addThisMonthPurchase(p)
        }
    }

    private fun resetInputFields() {
        dateEditText.setText("")
        placeEditText.setText("")
        amountTextView.setText("")
        categorySpinner.setSelection(0)
    }

    private fun refreshMonitorData() {
        for (p: Purchase in purchases.thisMonthPurchases) {
            if (p.purchaseDate.month != Date().month) {
                purchases.removePurchaseById(p.id)
            }
        }
    }

    private fun updateLabel() {
        val myFormat = "yyyy. MM. dd."
        val dateFormat = SimpleDateFormat(myFormat)
        dateEditText.setText(dateFormat.format(myCalendar.time))
    }

}