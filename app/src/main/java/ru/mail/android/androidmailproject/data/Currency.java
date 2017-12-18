package ru.mail.android.androidmailproject.data;

import android.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class Currency {
    private String name;
    private Integer state;
    private Map<Pair<String, String>, Float> rates;

    Currency(String name, Integer state) {
        this.name = name;
        this.state = state;

        this.rates = new HashMap<>();
    }

    void changeState() {
        state = 1 - state;
    }

    Integer getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    Float getRate(String date, String toCompare) {
        return rates.get(new Pair<>(date, toCompare));
    }

    void addRates(String date, Map<String, Float> rates) {
        for (Map.Entry<String, Float> entry : rates.entrySet())
            this.rates.put(new Pair<>(date, entry.getKey()), entry.getValue());
    }

    boolean hasInfo(String date, String toCompare) {
        return rates.containsKey(new Pair<>(date, toCompare));
    }

    public Map<Pair<String, String>, Float> getRates() {return rates;}
}
