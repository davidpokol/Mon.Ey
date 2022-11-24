package com.example.money.model;


public class Settings {

    private static Integer monthLimit = 0;
    private static int currencyIndex = 0;
    private static int favCategoryIndex = 0;
    private static boolean EnabledSoundEffects = true;

    public Settings() {
    }

    public Integer getMonthLimit() {
        return monthLimit;
    }

    public void setMonthLimit(Integer goal) {
        Settings.monthLimit = goal;
    }

    public Integer getCurrencyIndex() {
        return currencyIndex;
    }

    public void setCurrencyIndex(Integer currency) {
        Settings.currencyIndex = currency;
    }

    public int getFavCategoryIndex() {
        return favCategoryIndex;
    }

    public void setFavCategoryIndex(Integer favCategoryIndex) {
        Settings.favCategoryIndex = favCategoryIndex;
    }

    public boolean isEnabledSoundEffects() {
        return EnabledSoundEffects;
    }

    public void setEnabledSoundEffects(boolean enabledSoundEffects) {
        EnabledSoundEffects = enabledSoundEffects;
    }
}