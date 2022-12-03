package com.example.money.util

import com.example.money.model.Purchase
import com.example.money.model.Purchases
import java.util.*

class PurchaseUtil {
    private val dateTimeUtil = DateTimeUtil()
    private val purchases = Purchases()
    private lateinit var thisMonthPurchases : MutableList<Purchase>

    fun getMonthPurchases(monthDate: Date) : List<Purchase> {
        thisMonthPurchases = mutableListOf()
        for (purchase in purchases.purchaseList) {
            if (dateTimeUtil.getMonth(monthDate) == dateTimeUtil.getMonth(purchase.purchaseDate)) {
                thisMonthPurchases.add(purchase)
            }
        }
         return thisMonthPurchases
    }
    fun setupThisMonthPurchases() {
        purchases.thisMonthPurchases = getMonthPurchases(Date())
    }
}