package ru.mail.android.androidmailproject.dataSingltones;

import android.util.Pair;

import java.util.HashMap;
import java.util.Map;

import ru.mail.android.androidmailproject.JsonModels.Currencies;

/**
 * Created by sergey on 24.10.17.
 * Singletone for storage Currencies after uploading in StartActivity
 */

public class CurrenciesSingletone {
    private static CurrenciesSingletone instance;
    private Map<Pair<String, String>, Currencies> currencies = new HashMap<>();
    private String[] currenciesNames;
    private boolean isFilled;
    private String latest = "";

    public static CurrenciesSingletone getInstance() {
        if (instance == null) {
            instance = new CurrenciesSingletone();
        }
        return instance;
    }

    private CurrenciesSingletone() {
        isFilled = false;
    }

    public void addCurrency(Currencies currencies, boolean isLatest) {
        if (isLatest)
            latest = currencies.getDate();
        if (!this.currencies.containsKey(new Pair<String, String>(currencies.getBase(), currencies.getDate())))
            this.currencies.put(new Pair<String, String>(currencies.getBase(), currencies.getDate()), currencies);
    }

    public void fillCurrenciesNames(Currencies currencies) {
        isFilled = true;
        Map<String, Float> map = currencies.getRates();
        int i = 0;

        currenciesNames = new String[map.size() + 1];
        currenciesNames[0] = currencies.getBase();

        for(Map.Entry entry: map.entrySet()) {
            i++;
            currenciesNames[i] = (String)entry.getKey();
        }
    }

    public Currencies getCurrencyInfo(String name, String date) {
        if (date.equals("latest"))
            return currencies.get(new Pair<String, String>(name, latest));
        return currencies.get(new Pair<String, String>(name, date));
    }

    public boolean hasInfo(String name, String date) {
        return currencies.containsKey(new Pair<String, String>(name, date));
    }

    public String[] getCurrenciesNames() {
        return currenciesNames;
    }
}
