package com.example.money.model;


public class Settings {

    private Integer monthLimit = 0;
    private int currencyIndex = 0;
    private int favCategoryIndex = 0;
    private boolean EnabledSoundEffects = true;

    public Settings() {
    }

    public Integer getMonthLimit() {
        return monthLimit;
    }

    public void setMonthLimit(Integer goal) {
        this.monthLimit = goal;
    }

    public Integer getCurrencyIndex() {
        return currencyIndex;
    }

    public void setCurrencyIndex(Integer currency) {
        this.currencyIndex = currency;
    }

    public int getFavCategoryIndex() {
        return favCategoryIndex;
    }

    public void setFavCategoryIndex(Integer favCategoryIndex) {
        this.favCategoryIndex = favCategoryIndex;
    }

    public boolean isEnabledSoundEffects() {
        return EnabledSoundEffects;
    }

    public void setEnabledSoundEffects(boolean enabledSoundEffects) {
        EnabledSoundEffects = enabledSoundEffects;
    }
}