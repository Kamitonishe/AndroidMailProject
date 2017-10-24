package ru.mail.android.androidmailproject.dataSingltones;

import java.util.Map;

import ru.mail.android.androidmailproject.JsonModels.Currencies;

/**
 * Created by sergey on 24.10.17.
 * Singletone for storage Currencies after uploading in StartActivity
 */

public class CurrenciesSingletone {
    private static CurrenciesSingletone instance;
    private Currencies currencies;
    private String[] stringCurrencies;
    private boolean isFilled;

    public static CurrenciesSingletone getInstance() {
        if (instance == null) {
            instance = new CurrenciesSingletone();
        }
        return instance;
    }

    private CurrenciesSingletone() {
        isFilled = false;
    }

    public void setCurrencies(Currencies currencies) {
        this.currencies = currencies;

        //initialization of String Currencies for RecyclerView in MainActivity
        Map<String, Float> map = currencies.getRates();
        int i = 0;
        stringCurrencies = new String[map.size() + 1];
        stringCurrencies[0] = currencies.getBase();
        for(Map.Entry entry: map.entrySet()) {
            i++;
            stringCurrencies[i] = (String)entry.getKey();
        }


        isFilled = true;
    }

    public Currencies getCurrencies() {
        return currencies;
    }


    //returns String Currencies for RecyclerView in MainActivity
    public String[] getStringCurrencies() {
        return stringCurrencies;
    }
}
