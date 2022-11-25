package com.example.money.util

class DataUtil {

    fun convertBoolToInt(isChecked: Boolean) : Int {

        return if (isChecked) {
            1
        } else {
            0
        }
    }
}