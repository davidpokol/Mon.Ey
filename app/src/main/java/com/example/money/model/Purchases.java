package com.example.money.model;

import java.util.ArrayList;
import java.util.List;

public class Purchases {

    private final List<Purchase> purchaseList = new ArrayList<>();
    private List<Purchase> thisMonthPurchases = new ArrayList<>();

    public Purchases() {
    }

    public List<Purchase> getPurchaseList() {
        return purchaseList;
    }

    public void addPurchase(Purchase purchase) {
        purchaseList.add(purchase);
    }

    public Purchase getPurchaseById(int id) {
        for (Purchase p : purchaseList) {
            if(p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public void setThisMonthPurchases(List<Purchase> thisMonthPurchases) {
        this.thisMonthPurchases = thisMonthPurchases;
    }

    public List<Purchase> getThisMonthPurchases() {
        return thisMonthPurchases;
    }

    public void removePurchaseById(int id) {
        for (Purchase p : purchaseList) {
            if(p.getId() == id) {
                purchaseList.remove(p);
                return;
            }
        }
    }
}