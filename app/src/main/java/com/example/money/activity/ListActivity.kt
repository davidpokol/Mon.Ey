package com.example.money.activity

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.gridlayout.widget.GridLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.example.money.R
import com.example.money.db.MyDBHelper
import com.example.money.model.Purchase
import com.example.money.model.Settings
import com.example.money.util.DateTimeUtil
import com.example.money.util.StringUtil

@SuppressLint("Recycle")
class ListActivity : AppCompatActivity() {

    private lateinit var myDBHelper: MyDBHelper
    private lateinit var selectMyDB: SQLiteDatabase
    private lateinit var myDB: SQLiteDatabase
    private lateinit var gridLayout: GridLayout
    private lateinit var previousButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var deleteButton: ImageButton

    private var dateTimeUtil = DateTimeUtil()
    private var stringUtil = StringUtil()

    private lateinit var rs: Cursor
    private lateinit var settingsRs: Cursor
    private var purchases = mutableListOf<Purchase>()

    private var i = 0
    private var currency = "forint"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        supportActionBar?.hide()

        myDBHelper = MyDBHelper(this)
        myDB = myDBHelper.readableDatabase
        val soundEffects = isSoundEffectsOn()
        rs = myDB.rawQuery("SELECT * FROM purchases", null)
        settingsRs = myDB.rawQuery("SELECT currency_index FROM settings", null)

        gridLayout = findViewById(R.id.gridLayout)
        val dateTextView = findViewById<TextView>(R.id.resultDateTextView)
        val placeTextView = findViewById<TextView>(R.id.resultPlaceTextView)
        val amountTextView = findViewById<TextView>(R.id.resultAmountTextView)
        val categoryTextView = findViewById<TextView>(R.id.resultCategoryTextView)

        previousButton = findViewById(R.id.email_button)
        nextButton = findViewById(R.id.nextButton)
        deleteButton = findViewById(R.id.deleteButton)

        if (!loadPurchasesFromDataBase()) {
            return
        }

        if (settingsRs.moveToFirst()) {
            currency = resources.getStringArray(R.array.currencies)[settingsRs.getInt(0)]
                .toString().toLowerCase()
            settingsRs.close()
        }

        dateTextView.text = dateTimeUtil.dateToString(purchases[i].purchaseDate)
        placeTextView.text = purchases[i].place
        amountTextView.text = stringUtil.formatAmount(purchases[i].amount.toString()
                ) +  " " + currency
        categoryTextView.text =
            resources.getStringArray(R.array.categories)[purchases[i].categoryIndex]

        manageButtons()
        nextButton.setOnClickListener {

            if (i < purchases.count()) {
                i++
                dateTextView.text = dateTimeUtil.dateToString(purchases[i].purchaseDate)
                placeTextView.text = purchases[i].place
                amountTextView.text = stringUtil.formatAmount(purchases[i].amount.toString()
                ) +  " " + currency
                categoryTextView.text =
                    resources.getStringArray(R.array.categories)[purchases[i].categoryIndex]
            }
            manageButtons()
        }

        previousButton.setOnClickListener {

            if (i > 0) {
                i--
                dateTextView.text = dateTimeUtil.dateToString(purchases[i].purchaseDate)
                placeTextView.text = purchases[i].place
                amountTextView.text = stringUtil.formatAmount(purchases[i].amount.toString()
                ) +  " " + currency
                categoryTextView.text =
                    resources.getStringArray(R.array.categories)[purchases[i].categoryIndex]
            }
            manageButtons()
        }

        deleteButton.setOnClickListener {
            val alert = AlertDialog.Builder(this)
            alert.setTitle(getString(R.string.delete_purchase))
            alert.setMessage(getString(R.string.are_you_sure_deleting))
            alert.setPositiveButton(
                getString(R.string.positive_button_yes),
                DialogInterface.OnClickListener { _, _ ->

                    var a = 0
                    if (purchases.count() >= 2) {

                        if (i == 0) { // ha az első elem
                            dateTextView.text =
                                dateTimeUtil.dateToString(purchases[i + 1].purchaseDate)

                            placeTextView.text = purchases[i + 1].place

                            amountTextView.text =
                                stringUtil.formatAmount(purchases[i + 1].amount.toString()
                                        ) +  " " + currency

                            categoryTextView.text = resources.
                            getStringArray(R.array.categories)[purchases[i + 1].categoryIndex]

                        } else {
                            a = i--
                            dateTextView.text = dateTimeUtil.dateToString(purchases[i].purchaseDate)

                            placeTextView.text = purchases[i].place

                            amountTextView.text =
                                stringUtil.formatAmount(purchases[i].amount.toString()
                                ) +  " " + currency

                            categoryTextView.text = resources.
                            getStringArray(R.array.categories)[purchases[i].categoryIndex]
                        }
                    }
                    if (soundEffects) {
                        deleteButton.isSoundEffectsEnabled = false
                        MediaPlayer.create(this, R.raw.delete_sound).start()
                        deleteButton.isSoundEffectsEnabled = true
                    }

                    myDB.delete("purchases", "id = ?",
                        arrayOf(purchases[a].id.toString()))
                    rs.requery()

                    loadPurchasesFromDataBase()
                    manageButtons()

                })

            alert.setNegativeButton(getString(R.string.negative_button),
                DialogInterface.OnClickListener { _, _ ->
                })
            alert.show()
        }
    }

    override fun onBackPressed() {
        selectMyDB.close()
        myDB.close()
        super.onBackPressed()
        this.finish()
    }

    private fun manageButtons() {

        if (i == 0) {
            previousButton.backgroundTintList = getColorStateList(R.color.disabled_grey)
            previousButton.isEnabled = false
        } else {
            previousButton.backgroundTintList = getColorStateList(R.color.money_dark_green)
            previousButton.isEnabled = true
        }

        if (i == purchases.count() - 1) {
            nextButton.backgroundTintList = getColorStateList(R.color.disabled_grey)
            nextButton.isEnabled = false
        } else {
            nextButton.backgroundTintList = getColorStateList(R.color.money_dark_green)
            nextButton.isEnabled = true
        }
    }

    private fun loadPurchasesFromDataBase(): Boolean {
        purchases.clear()
        if (rs.moveToFirst()) {
            do {
                val purchase = Purchase()
                purchase.id = rs.getInt(0)
                purchase.purchaseDate = dateTimeUtil.stringToDate(rs.getString(1))
                purchase.place = rs.getString(2)
                purchase.amount = rs.getInt(3)
                purchase.categoryIndex = rs.getInt(4)
                purchases.add(purchase)

            } while (rs.moveToNext())

            return true
        } else {
            gridLayout.isVisible = false
            previousButton.isVisible = false
            nextButton.isVisible = false
            deleteButton.isVisible = false
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
            return false
        }
    }

    private fun isSoundEffectsOn() : Boolean {
        var result = true
        selectMyDB = myDBHelper.readableDatabase

        val rs = selectMyDB.rawQuery("SELECT enabled_sound_effects " +
                "FROM settings", null)

        if (rs.moveToFirst()) {
            result = rs.getInt(0) == 1
        } else {
            Toast.makeText(this,
                getString(R.string.database_error_toast), Toast.LENGTH_LONG).show()
        }
        return result
    }
}