package com.example.money.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import java.text.DecimalFormat
import java.text.NumberFormat


class StringUtil {

    fun onTextChangedListener(editText: EditText): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if (editText.text.toString().isNotEmpty()) {
                        editText.removeTextChangedListener(this)
                        editText.setText(formatAmount(editText.text.toString()))
                        editText.setSelection(editText.text.length)
                        editText.addTextChangedListener(this)
                    }
                } catch (_: Exception) {
                }
            }
        }
    }

    private val formatter: DecimalFormat = NumberFormat.getInstance() as DecimalFormat
    private val groupingSeparator: String =
        formatter.decimalFormatSymbols.groupingSeparator.toString()

    fun formatAmount(amount: String): String {

        val originalString = deFormatAmount(amount.trim())
        val longVal: Long = originalString.toLong()
        formatter.applyPattern("#,###,###,###")
        return formatter.format(longVal)
    }


    fun deFormatAmount(s: String): Int {

        if (s.contains(groupingSeparator)) {
            return s.replace(groupingSeparator, "").toInt()
        }
        return s.toInt()
    }

}