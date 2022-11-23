package com.example.money.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.money.R
import com.example.money.model.Settings
import com.example.money.model.SpinnerValues
import com.example.money.util.StringUtil


class SettingsActivity : AppCompatActivity() {

    private val settings = Settings()
    private val stringUtil = StringUtil()

    private lateinit var goalEditText: EditText
    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.hide()
        /*
        val helper = MyDBHelper(this)
        val db = helper.readableDatabase
        val rs = db.rawQuery("SELECT * FROM settings", null)

        if (rs.moveToFirst()) {

            settings.goal = rs.getInt(1)
            //TODO többi adat
        } else {
            Toast.makeText(this,
                getString(R.string.database_error_toast), Toast.LENGTH_SHORT).show()
        }
        */

        val currencySpinner: Spinner = findViewById(R.id.currency_spinner)
        goalEditText = findViewById(R.id.editTextNumber)
        goalEditText.addTextChangedListener(stringUtil.onTextChangedListener(goalEditText))

        if (settings.monthLimit != null && settings.monthLimit != 0) {
            goalEditText.setText(stringUtil.formatAmount(settings.monthLimit.toString()))
        }

        currencySpinner.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.currencies)
        )

        currencySpinner.setSelection(settings.currencyIndex)

        goalEditText.addTextChangedListener {
            try {
                if (goalEditText.text.isEmpty()) {
                    settings.monthLimit = 0
                } else {
                    settings.monthLimit = Integer.valueOf(
                        stringUtil.deFormatAmount(
                            goalEditText.text.toString())
                    )
                }
            } catch (_: Exception) {}
        }

        currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                settings.currencyIndex = currencySpinner.selectedItemPosition
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}