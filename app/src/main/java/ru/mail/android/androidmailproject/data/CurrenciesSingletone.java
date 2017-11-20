package ru.mail.android.androidmailproject.data;


import android.support.v4.util.Pair;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.mail.android.androidmailproject.JsonModels.Currencies;
import ru.mail.android.androidmailproject.auxiliary.DateManager;

/**.
 * Singletone for storage Currencies after uploading in StartActivity
 */

public class CurrenciesSingletone {
    private static CurrenciesSingletone instance;

    List<CurrenciesListener> listeners = new ArrayList<>();

    private Map<String, Currency> currencies_;
    private Map<String, String> latestFeaturedDate;

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
        latestFeaturedDate = new HashMap<>();
        currencies_ = new HashMap<>();
    }

    public void addCurrency(Currencies currencies, boolean isLatest) {
        synchronized (CurrenciesSingletone.class) {
            if (isLatest)
                latest = currencies.getDate();

            if (this.currencies_.containsKey(currencies.getBase()))
                this.currencies_.get(currencies.getBase()).addRates(currencies.getDate(), currencies.getRates());

            if (!latestFeaturedDate.containsKey(currencies.getBase()) ||
                    DateManager.isLaterThan(currencies.getDate(), latestFeaturedDate.get(currencies.getBase())))
                latestFeaturedDate.put(currencies.getBase(), currencies.getDate());
            }
    }

    public void fillCurrenciesNames(Currencies currencies) {
        synchronized (CurrenciesSingletone.class) {
            Map<String, Float> map = currencies.getRates();

            for (Map.Entry<String, Float> entry : map.entrySet())
                currencies_.put(entry.getKey(), new Currency(entry.getKey(), 0));

        }
    }

    public void fillCurrenciesNames(ArrayList<Pair<String, Integer>> names) {
        synchronized (CurrenciesSingletone.class) {

            for (int i = 0; i < names.size(); ++i)
                currencies_.put(names.get(i).first, new Currency(names.get(i).first, names.get(i).second));
        }
    }

    public Float getCurrencyRate(String base, String date, String toCompare) {
        synchronized (CurrenciesSingletone.class) {
            if (date.equals("latest"))
                return currencies_.get(base).getRate(date, toCompare);
            return currencies_.get(base).getRate(date, toCompare);
        }
    }

    public boolean hasInfo(String base, String date, String toCompare) {
        synchronized (CurrenciesSingletone.class) {
            return currencies_.get(base).hasInfo(date, toCompare);
        }
    }

    public boolean hasInfo(String name) {
        synchronized (CurrenciesSingletone.class) {
            return latestFeaturedDate.containsKey(name);
        }
    }

    public android.util.Pair<String, Integer>[] getCurrenciesNamesAndStates() {
        synchronized (CurrenciesSingletone.class) {
            android.util.Pair<String, Integer>[] ans = new android.util.Pair[currencies_.size()];
            int i = 0;
            for (Currency cur : currencies_.values())
                ans[i++] = new android.util.Pair<>(cur.getName(), cur.getState());

            Arrays.sort(ans, new Comparator<android.util.Pair<String, Integer>>() {
                @Override
                public int compare(android.util.Pair<String, Integer> st1, android.util.Pair<String, Integer> st2) {
                    return -st1.second.compareTo(st2.second);
                }
            });

            return ans;
        }
    }

    public String getLatestFeaturedDate(String base) {
        return latestFeaturedDate.get(base);
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
        currencies_.get(s).changeState();
    }
}
