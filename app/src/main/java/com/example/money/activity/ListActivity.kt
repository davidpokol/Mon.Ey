package com.example.money.activity

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.money.R
import com.example.money.db.MyDBHelper
import com.example.money.model.Purchase
import com.example.money.util.DateTimeUtil
import com.example.money.util.StringUtil

@SuppressLint("Recycle")
class ListActivity : AppCompatActivity() {

    private lateinit var myDBHelper: MyDBHelper
    private lateinit var myDB: SQLiteDatabase

    private var dateTimeUtil = DateTimeUtil()
    private var stringUtil = StringUtil()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        supportActionBar?.hide()

        myDBHelper = MyDBHelper(this)
        myDB = myDBHelper.readableDatabase

        val dateTextView = findViewById<TextView>(R.id.resultDateTextView)
        val placeTextView = findViewById<TextView>(R.id.resultPlaceTextView)
        val amountTextView = findViewById<TextView>(R.id.resultAmountTextView)
        val categoryTextView = findViewById<TextView>(R.id.resultCategoryTextView)

        val previousButton = findViewById<ImageButton>(R.id.previousButton)
        val nextButton = findViewById<ImageButton>(R.id.nextButton)


        val rs = myDB.rawQuery("SELECT * FROM purchases", null)

        val purchase = Purchase()
        if (rs.moveToFirst()) {

            purchase.id = rs.getInt(0)
            purchase.purchaseDate = dateTimeUtil.stringToDate(rs.getString(1))
            purchase.place = rs.getString(2)
            purchase.amount = rs.getInt(3)
            purchase.categoryIndex = rs.getInt(4)

        } else {
            Toast.makeText(this,"No data!", Toast.LENGTH_SHORT).show()
            previousButton.isEnabled = false
            nextButton.isEnabled = false
            return
        }

        dateTextView.text = dateTimeUtil.dateToString(purchase.purchaseDate)
        placeTextView.text = purchase.place
        amountTextView.text = stringUtil.formatAmount(purchase.amount.toString())
        categoryTextView.text = resources.getStringArray(R.array.categories)[purchase.categoryIndex + 1]


        nextButton.setOnClickListener {

            if (rs.moveToNext()) {

                purchase.id = rs.getInt(0)
                purchase.purchaseDate = dateTimeUtil.stringToDate(rs.getString(1))
                purchase.place = rs.getString(2)
                purchase.amount = rs.getInt(3)
                purchase.categoryIndex = rs.getInt(4)

            } else if(rs.moveToFirst()){

                purchase.id = rs.getInt(0)
                purchase.purchaseDate = dateTimeUtil.stringToDate(rs.getString(1))
                purchase.place = rs.getString(2)
                purchase.amount = rs.getInt(3)
                purchase.categoryIndex = rs.getInt(4)


            } else {
                Toast.makeText(this,"Database error!",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            dateTextView.text = dateTimeUtil.dateToString(purchase.purchaseDate)
            placeTextView.text = purchase.place
            amountTextView.text = stringUtil.formatAmount(purchase.amount.toString())
            categoryTextView.text = resources.getStringArray(R.array.categories)[purchase.categoryIndex + 1]
        }

        previousButton.setOnClickListener {

            if (rs.moveToPrevious()) {

                purchase.id = rs.getInt(0)
                purchase.purchaseDate = dateTimeUtil.stringToDate(rs.getString(1))
                purchase.place = rs.getString(2)
                purchase.amount = rs.getInt(3)
                purchase.categoryIndex = rs.getInt(4)

            } else {

            }

            dateTextView.text = dateTimeUtil.dateToString(purchase.purchaseDate)
            placeTextView.text = purchase.place
            amountTextView.text = stringUtil.formatAmount(purchase.amount.toString())
            categoryTextView.text = resources.getStringArray(R.array.categories)[purchase.categoryIndex]
        }
    }
}