package com.example.money.model;

import java.util.Date;

public class Purchase {

    private static int staticId = 0;
    private final int id;
    private final Date purchaseDate;
    private final String place;
    private final int amount;
    private final int categoryIndex;


    public Purchase(Date purchaseDate, String place, int amount, int categoryIndex) {
        this.id = ++staticId;
        this.purchaseDate = purchaseDate;
        this.place = place;
        this.amount = amount;
        this.categoryIndex = categoryIndex;
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

    public int getCategory() {
        return categoryIndex;
    }

    public int getAmount() {
        return amount;
    }
}
