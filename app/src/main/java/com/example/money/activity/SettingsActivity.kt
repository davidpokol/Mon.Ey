package com.example.money.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.money.enums.Currency
import com.example.money.model.Money
import com.example.money.R


class SettingsActivity : AppCompatActivity() {

    private val money = Money()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.hide()

        val currencySpinner: Spinner = findViewById(R.id.currency_spinner)
        val goalEditText: EditText = findViewById(R.id.editTextNumber)

        currencySpinner.adapter = ArrayAdapter<Currency>(
            this, android.R.layout.simple_spinner_item, Currency.values())

        goalEditText.addTextChangedListener {
            money.goal = Integer.valueOf(goalEditText.text.toString())
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