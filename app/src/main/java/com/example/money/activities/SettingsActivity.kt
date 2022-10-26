package com.example.money.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.money.enums.Currency
import com.example.money.model.Money
import com.example.money.R


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.hide()



        val currencySpinner: Spinner = findViewById(R.id.currency_spinner)
        val goalEditText: EditText = findViewById(R.id.editTextNumberSigned)

        currencySpinner.adapter = ArrayAdapter<Currency>(
            this, android.R.layout.simple_spinner_item, Currency.values())
        goalEditText.setOnFocusChangeListener { _, hasFocus ->

            if(!hasFocus) {
                Money.setGoal(Integer.getInteger(goalEditText.text.toString()))
            }
        }
        currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                Money.setCurrency(currencySpinner.selectedItem as Currency)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }
}