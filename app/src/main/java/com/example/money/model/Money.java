package com.example.money.model;

import com.example.money.enums.Currency;

public class Money {

    private static Integer goal = 1;
    private static int moneySpent = 1;
    private static Currency currency = Currency.FORINT;

    public Money() {
    }

    public static Integer getGoal() {
        return goal;
    }

    public static void setGoal(Integer goal) {
        Money.goal = goal;
    }

    public static int getMoneySpent() {
        return moneySpent;
    }

    public static int getSpendableMoney() {
        return goal - moneySpent;
    }

    public static void setMoneySpent(int moneySpent) {
        Money.moneySpent = moneySpent;
    }

    public static Currency getCurrency() {
        return currency;
    }

    public static void setCurrency(Currency currency) {
        Money.currency = currency;
    }
}