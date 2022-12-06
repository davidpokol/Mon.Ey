package com.example.money.util

import com.example.money.model.Purchase
import java.util.*

class PurchaseUtil {
    private val dateTimeUtil = DateTimeUtil()

    fun getMonthPurchases(list: List<Purchase>, monthDate: Date) : List<Purchase> {
        val result = mutableListOf<Purchase>()
        for (purchase in list) {
            if (dateTimeUtil.getMonth(monthDate) == dateTimeUtil.getMonth(purchase.purchaseDate)) {
                result.add(purchase)
            }
        }
         return result
    }
    fun getThisMonthPurchases(list: List<Purchase>) : List<Purchase> {
        return getMonthPurchases(list, Date())
    }
}