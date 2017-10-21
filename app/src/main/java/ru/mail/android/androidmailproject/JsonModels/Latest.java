package ru.mail.android.androidmailproject.JsonModels;

import android.icu.util.Calendar;

import java.util.List;

/**
 * Created by Franck on 21.10.2017.
 */

public class Latest {
    private String base;
    private Calendar data;
    private List<Rates> rates;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Calendar getData() {
        return data;
    }

    public void setData(Calendar data) {
        this.data = data;
    }

    public List<Rates> getRates() {
        return rates;
    }

    public void setRates(List<Rates> rates) {
        this.rates = rates;
    }
}
