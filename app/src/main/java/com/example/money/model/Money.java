package com.example.money.model;

import com.example.money.enums.Currency;

public class Money {

    private static int goal = 0;
    private static Currency currency = Currency.FORINT;

    public Money() {
    }

    public static int getGoal() {
        return goal;
    }

    public static void setGoal(int goal) {
        Money.goal = goal;
    }

    public static Currency getCurrency() {
        return currency;
    }

    public static void setCurrency(Currency currency) {
        Money.currency = currency;
    }
}
