package com.example.money.model;

import java.util.Date;

public class Purchase {

    private int id;
    private Date purchaseDate;
    private String place;
    private int amount;
    private int categoryIndex;

    public Purchase() {
    }

    public Purchase(Date purchaseDate, String place, int amount, int categoryIndex) {
        this.purchaseDate = purchaseDate;
        this.place = place;
        this.amount = amount;
        this.categoryIndex = categoryIndex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCategoryIndex() {
        return categoryIndex;
    }

    public void setCategoryIndex(int categoryIndex) {
        this.categoryIndex = categoryIndex;
    }
}
