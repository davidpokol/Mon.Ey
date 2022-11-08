package com.example.money.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class StringUtil {

    fun onTextChangedListener(editText: EditText): TextWatcher? {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                editText.removeTextChangedListener(this)
                try {
                    var originalString = s.toString()
                    val longval: Long
                    if (originalString.contains(",")) {
                        originalString = originalString.replace(",".toRegex(), "")
                    }
                    longval = originalString.toLong()
                    val formatter: DecimalFormat =
                        NumberFormat.getInstance(Locale.US) as DecimalFormat
                    formatter.applyPattern("#,###,###,###")
                    val formattedString: String = formatter.format(longval)

                    //setting text after format to EditText
                    editText.setText(formattedString)
                    editText.setSelection(editText.text.length)
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }
                editText.addTextChangedListener(this)
            }
        }
    }
    fun formatAmount(amount: Int): String {
        val s = StringBuilder(amount.toString())
        if (s.length < 3) {
            return s.toString()
        }
        var count = 0
        for (i in s.length downTo 1) {
            if (count == 3) {
                s.insert(i, ",")
                count = 0
            }
            count++
        }
        return s.toString()
    }


    fun deFormatAmount(s: String): Int {
        return s.replace(",", "").toInt()
    }

}