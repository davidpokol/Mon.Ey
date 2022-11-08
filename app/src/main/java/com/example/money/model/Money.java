package com.example.money.model;
import com.example.money.enums.Currency;


public class Money {

    private static Integer goal = 0;
    private static Currency currency = Currency.FORINT;

    public Money() {
    }

    public Integer getGoal() {
        return goal;
    }

    public void setGoal(Integer goal) {
        Money.goal = goal;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        Money.currency = currency;
    }

}