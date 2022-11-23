package com.example.money.model;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.money.R;

import java.util.Arrays;
import java.util.List;

public class SpinnerValues {

    Context context;
    private final String[] currencyArray = context.getResources().getStringArray(R.array.currencies);
    private final String[] categoryArray = context.getResources().getStringArray(R.array.categories);

    public SpinnerValues(Context context) {
        this.context = context;
    }

    public List<String> getCurrencyList() {
        return Arrays.asList(currencyArray);
    }

    public List<String> getCategoryList() {
        return Arrays.asList(categoryArray);
    }

    public String getCurrency(int index) {
        return currencyArray[index];
    }

    public String getCategory(int index) {
        return categoryArray[index];
    }
}
