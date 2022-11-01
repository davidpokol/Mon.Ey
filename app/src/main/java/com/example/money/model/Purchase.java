package com.example.money.model;

import com.example.money.enums.Category;

import java.util.Date;
import com.example.money.enums.Currency;

public class Purchase {

    private static int staticId = 0;
    private int id;
    private final Date purchaseDate;
    private final String place;
    private final int amount;
    private Currency currency;
    private final Category category;


    public Purchase(Date purchaseDate, String place, int amount, Currency currency, Category category) {
        this.id += staticId;
        this.purchaseDate = purchaseDate;
        this.place = place;
        this.amount = amount;
        this.currency = currency;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public String getPlace() {
        return place;
    }

    public Category getCategory() {
        return category;
    }

    public int getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }
}
