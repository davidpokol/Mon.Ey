package com.example.money.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.money.R
import com.example.money.enums.Currency
import com.example.money.model.Money
import com.example.money.util.StringUtil
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


class SettingsActivity : AppCompatActivity() {

    private val money = Money()
    private val stringUtil  = StringUtil()

    private lateinit var goalEditText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        goalEditText = findViewById(com.example.money.R.id.editTextNumber)
        supportActionBar?.hide()
        goalEditText.addTextChangedListener(stringUtil.onTextChangedListener(goalEditText))
        val currencySpinner: Spinner = findViewById(R.id.currency_spinner)

        if (money.goal != null && money.goal != 0) {
            goalEditText.setText(stringUtil.formatAmount(money.goal))
        }

        currencySpinner.adapter = ArrayAdapter<Currency>(
            this, android.R.layout.simple_spinner_item, Currency.values())

        currencySpinner.setSelection(Currency.values().indexOf(money.currency))

        goalEditText.addTextChangedListener {
            try{

                money.goal = Integer.valueOf(stringUtil.deFormatAmount(
                    goalEditText.text.toString()))
            } catch ( _ : Exception) {}

        }

        currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                money.currency = currencySpinner.selectedItem as Currency
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

}