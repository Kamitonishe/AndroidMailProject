package ru.mail.android.androidmailproject.JsonModels;

import android.icu.util.Calendar;

import java.util.List;
import java.util.Map;

/**
 * Created by Franck on 21.10.2017.
 */

public class Currencies {
    private String base;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, Float> getRates() {
        return rates;
    }

    public void setRates(Map<String, Float> rates) {
        this.rates = rates;
    }

    private String date;
    private Map<String, Float> rates;


}
