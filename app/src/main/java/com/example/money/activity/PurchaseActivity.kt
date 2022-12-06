package com.example.money.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.money.R
import com.example.money.db.MyDBHelper
import com.example.money.model.Settings
import com.example.money.util.DateTimeUtil
import com.example.money.util.StringUtil
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("Recycle", "SimpleDateFormat")
class PurchaseActivity : AppCompatActivity() {

    private val settings = Settings()
    private val dateTimeUtil = DateTimeUtil()
    private val stringUtil = StringUtil()
    private val myCalendar = Calendar.getInstance()

    private lateinit var dateEditText: EditText
    private lateinit var placeEditText: EditText
    private lateinit var amountTextView: EditText
    private lateinit var categorySpinner: Spinner

    private lateinit var myDBHelper: MyDBHelper
    private lateinit var insertMyDB: SQLiteDatabase
    private lateinit var selectMyDB: SQLiteDatabase

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase)
        supportActionBar?.hide()

        myDBHelper = MyDBHelper(this)
        getDBData()

        val submitButton: Button = findViewById(R.id.submitButton)
        dateEditText = findViewById(R.id.dateEditText)
        placeEditText = findViewById(R.id.purchasePlaceEditText)
        amountTextView = findViewById(R.id.amountEditText)
        categorySpinner = findViewById(R.id.categorySpinner)

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

        placeEditText.setOnFocusChangeListener { _ , _ ->
            placeEditText.setText(placeEditText.text.toString().trim())
        }


        amountTextView.addTextChangedListener(
            stringUtil.onTextChangedListener(amountTextView)
        )

        categorySpinner.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.categories)
        )

        categorySpinner.setSelection(settings.favCategoryIndex)

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

            val cv = ContentValues()
            cv.put("date",dateTimeUtil.dateToString(date))
            cv.put("place", purchasePlace)
            cv.put("amount", amount)
            cv.put("category", categorySpinner.selectedItemPosition)


            insertMyDB = myDBHelper.readableDatabase
            insertMyDB.insert("purchases",null, cv)
            insertMyDB.close()
            if (settings.isEnabledSoundEffects) {
                submitButton.isSoundEffectsEnabled = false
                MediaPlayer.create(this, R.raw.cash_machine).start()
                submitButton.isSoundEffectsEnabled = true
            }

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

    private fun getDBData() {
        selectMyDB = myDBHelper.readableDatabase

        val rs = selectMyDB.rawQuery("SELECT fav_category_index, enabled_sound_effects " +
                "FROM settings", null)

        if (rs.moveToFirst()) {
            settings.favCategoryIndex = rs.getInt(0)
            settings.isEnabledSoundEffects = rs.getInt(1) == 1
        } else {
            Toast.makeText(this,
                getString(R.string.database_error_toast), Toast.LENGTH_LONG).show()
        }
        selectMyDB.close()
    }

    private fun resetInputFields() {
        dateEditText.setText("")
        placeEditText.setText("")
        amountTextView.setText("")
        categorySpinner.setSelection(settings.favCategoryIndex)
    }

    private fun updateLabel() {
        val myFormat = "yyyy. MM. dd."
        val dateFormat = SimpleDateFormat(myFormat)
        dateEditText.setText(dateFormat.format(myCalendar.time))
    }
}