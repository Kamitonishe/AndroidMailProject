package ru.mail.android.androidmailproject.JsonModels;

/**
 * Created by Franck on 21.10.2017.
 */

public class Rates {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    private double value;
}
