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
               try {
                    if(editText.text.toString().isNotEmpty()){

                        editText.removeTextChangedListener(this)
                        editText.setText(formatAmount(s.toString()))
                        editText.setSelection(editText.text.length)
                    }
               } catch ( _ : Exception) {} finally {
                   editText.addTextChangedListener(this)
               }

            }
        }
    }
    fun formatAmount(amount: String): String {

        val originalString = deFormatAmount(amount.trim())
        val longVal: Long = originalString.toLong()
        val formatter: DecimalFormat =
            NumberFormat.getInstance() as DecimalFormat
        formatter.applyPattern("#,###,###,###")
        return formatter.format(longVal)
    }


    fun deFormatAmount(s: String): Int {

        if (s.contains(",")) {
            return s.replace(",", "").toInt()
        }
        return s.toInt()
    }

}