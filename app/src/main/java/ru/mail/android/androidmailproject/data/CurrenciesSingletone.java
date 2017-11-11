package ru.mail.android.androidmailproject.data;


import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.mail.android.androidmailproject.JsonModels.Currencies;

/**.
 * Singletone for storage Currencies after uploading in StartActivity
 */

public class CurrenciesSingletone {
    private static CurrenciesSingletone instance;
    private Map<Pair<String, String>, Currencies> currencies;
    private Map<String, Integer> states;
    List<CurrenciesListener> listeners = new ArrayList<CurrenciesListener>();
    private String[] currenciesNames;
    private boolean isFilled;
    private String latest = "";

    public static CurrenciesSingletone getInstance() {
        synchronized (CurrenciesSingletone.class) {
            if (instance == null) {
                instance = new CurrenciesSingletone();
            }
            return instance;
        }
    }

    private CurrenciesSingletone() {
        isFilled = false;
        currencies = new HashMap<>();
        states = new HashMap<>();
    }

    public void addCurrency(Currencies currencies, boolean isLatest) {
        synchronized (CurrenciesSingletone.class) {
            if (isLatest)
                latest = currencies.getDate();
            if (!this.currencies.containsKey(new Pair<String, String>(currencies.getBase(), currencies.getDate())))
                this.currencies.put(new Pair<String, String>(currencies.getBase(), currencies.getDate()), currencies);
        }
    }

    public void fillCurrenciesNames(Currencies currencies) {
        synchronized (CurrenciesSingletone.class) {

            isFilled = true;
            Map<String, Float> map = currencies.getRates();
            int i = 0;

            currenciesNames = new String[map.size() + 1];
            currenciesNames[0] = currencies.getBase();
            states.put(currenciesNames[0], 0);

            for (Map.Entry entry : map.entrySet()) {
                i++;
                currenciesNames[i] = (String) entry.getKey();
                states.put(currenciesNames[i], 0);
            }
        }
    }

    public void fillCurrenciesNames(ArrayList<Pair<String, Integer>> names) {
        synchronized (CurrenciesSingletone.class) {

            isFilled = true;

            currenciesNames = new String[names.size()];

            for (int i = 0; i < names.size(); ++i) {
                currenciesNames[i] = names.get(i).first;
                states.put(names.get(i).first, names.get(i).second);
            }
        }
    }

    public Currencies getCurrencyInfo(String name, String date) {
        synchronized (CurrenciesSingletone.class) {
            if (date.equals("latest"))
                return currencies.get(new Pair<String, String>(name, latest));
            return currencies.get(new Pair<String, String>(name, date));
        }
    }

    public boolean hasInfo(String name, String date) {
        synchronized (CurrenciesSingletone.class) {
            return currencies.containsKey(new Pair<String, String>(name, date));
        }
    }

    public String[] getCurrenciesNames() {
        synchronized (CurrenciesSingletone.class) {
            return currenciesNames;
        }
    }

    public Map<String, Integer> getCurrenciesStates() {
        synchronized (CurrenciesSingletone.class) {
            return states;
        }
    }

    public void  addListener(CurrenciesListener l) {
        synchronized (CurrenciesSingletone.class) {
            listeners.add(l);
        }
    }

    public void removeListener(CurrenciesListener l) {
        synchronized (CurrenciesSingletone.class) {
            listeners.remove(l);
        }
    }

    private void notifyListeners() {
        synchronized (CurrenciesSingletone.class) {
            for (CurrenciesListener l : listeners) {
                l.updateCurrencies();
            }
        }
    }

    public void changeState(String s) {
        states.put(s, 1 - states.get(s));
    }
}
