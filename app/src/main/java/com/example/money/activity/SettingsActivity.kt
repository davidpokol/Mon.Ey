package com.example.money.activity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.money.R
import com.example.money.db.MyDBHelper
import com.example.money.model.Settings
import com.example.money.util.DataUtil
import com.example.money.util.StringUtil


class SettingsActivity : AppCompatActivity() {

    private val settings = Settings()
    private val stringUtil = StringUtil()
    private val dataUtil = DataUtil()

    private lateinit var limitEditText: EditText
    private lateinit var myDBHelper: MyDBHelper
    private lateinit var myDB: SQLiteDatabase

    @SuppressLint("Recycle", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.hide()

        myDBHelper = MyDBHelper(this)
        myDB = myDBHelper.readableDatabase
        val rs = myDB.rawQuery("SELECT * FROM settings", null)

        if (rs.moveToFirst()) {
            settings.monthLimit = rs.getInt(0)
            settings.currencyIndex = rs.getInt(1)
            settings.favCategoryIndex = rs.getInt(2)
            settings.isEnabledSoundEffects = rs.getInt(3) == 1
        } else {
            Toast.makeText(
                this,
                getString(R.string.database_error_toast), Toast.LENGTH_LONG
            ).show()
        }

        val currencySpinner: Spinner = findViewById(R.id.currency_spinner)
        val favCategorySpinner: Spinner = findViewById(R.id.fav_category_spinner)
        val soundEffectSwitch: Switch = findViewById(R.id.sound_effect_switch)


        limitEditText = findViewById(R.id.editTextNumber)
        limitEditText.addTextChangedListener(stringUtil.onTextChangedListener(limitEditText))

        if (settings.monthLimit != null && settings.monthLimit != 0) {
            limitEditText.setText(stringUtil.formatAmount(settings.monthLimit.toString()))
        }

        currencySpinner.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.currencies)
        )

        favCategorySpinner.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.categories)
        )

        currencySpinner.setSelection(settings.currencyIndex)
        favCategorySpinner.setSelection(settings.favCategoryIndex)

        limitEditText.addTextChangedListener {
            try {
                val cv = ContentValues()
                if (limitEditText.text.isEmpty()) {
                    cv.put("month_limit", 0)
                    //settings.monthLimit = 0
                } else {
                    cv.put(
                        "month_limit", Integer.valueOf(
                            stringUtil.deFormatAmount(
                                limitEditText.text.toString()
                            )
                        )
                    )
                }
                myDB.update("settings", cv, null, null)
            } catch (_: Exception) {
            }
        }

        currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                val cv = ContentValues()
                cv.put("currency_index", currencySpinner.selectedItemPosition)
                myDB.update("settings", cv, null, null)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        favCategorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                val cv = ContentValues()
                cv.put("fav_category_index", favCategorySpinner.selectedItemPosition)
                myDB.update("settings", cv, null, null)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        soundEffectSwitch.isChecked = settings.isEnabledSoundEffects

        soundEffectSwitch.setOnClickListener {

            val cv = ContentValues()
            cv.put("enabled_sound_effects", dataUtil.convertBoolToInt(soundEffectSwitch.isChecked))
            myDB.update("settings", cv, null, null)
        }
    }

    override fun onBackPressed() {
        myDB.close()
        super.onBackPressed()
        this.finish()
    }
}