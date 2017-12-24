package ru.mail.android.androidmailproject.data;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.mail.android.androidmailproject.JsonModels.Currencies;
import ru.mail.android.androidmailproject.auxiliary.CurrencyManager;
import ru.mail.android.androidmailproject.auxiliary.DateManager;

/**.
 * Singletone for storage Currencies after uploading in StartActivity
 */

public class CurrenciesSingletone {
    private static CurrenciesSingletone instance;
    private Context context;

    List<CurrenciesListener> listeners = new ArrayList<>();

    private Map<String, Currency> rated_currencies, other_currencies;
    private Map<String, String> latestFeaturedDate;
    private Map<String, String> currency_info;

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
        rated_currencies = new HashMap<>();
        other_currencies = new HashMap<>();
        currency_info = new HashMap<>();
    }

    public void setContext(Context context) {
        this.context = context;
    }


    private String loadJSON() {
        String json = null;
        try {
            InputStream is = context.getAssets().open("json/currency_info.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void loadCurrencyInfo() {
        try {
            JSONObject obj = new JSONObject(loadJSON());
            JSONArray m_jArry = obj.getJSONArray("currencies");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                String name = jo_inside.getString("name");
                String full_name = jo_inside.getString("full_name");
                String full_russian_name = jo_inside.getString("full_russian_name");
                String part100 = jo_inside.getString("part100");
                JSONArray countries_ = jo_inside.getJSONArray("countries");

                String[] countries = new String[countries_.length()];
                for (int j = 0 ; j < countries_.length(); ++j)
                    countries[j] = countries_.getString(j);

                currency_info.put(name, (new CurrencyManager(name, full_name, full_russian_name, part100, countries).getFormat()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCurrencyInfo(String name) {
        return currency_info.get(name);
    }


    public void addCurrency(Currencies currencies, boolean isLatest) {
        synchronized (CurrenciesSingletone.class) {
            if (isLatest)
                latest = currencies.getDate();

            if (this.rated_currencies.containsKey(currencies.getBase()))
                this.rated_currencies.get(currencies.getBase()).addRates(currencies.getDate(), currencies.getRates());
            else if (this.other_currencies.containsKey(currencies.getBase()))
                this.other_currencies.get(currencies.getBase()).addRates(currencies.getDate(), currencies.getRates());

            if (!latestFeaturedDate.containsKey(currencies.getBase()) ||
                    DateManager.isLaterThan(currencies.getDate(), latestFeaturedDate.get(currencies.getBase())))
                latestFeaturedDate.put(currencies.getBase(), currencies.getDate());
        }
    }

    public void fillCurrencies(Currencies currencies) {
        synchronized (CurrenciesSingletone.class) {
            Map<String, Float> map = currencies.getRates();

            for (Map.Entry<String, Float> entry : map.entrySet())
                this.other_currencies.put(entry.getKey(), new Currency(entry.getKey(), 0));

        }
    }

    public void fillCurrencies(ArrayList<Pair<String, Integer>> names) {
        synchronized (CurrenciesSingletone.class) {

            for (int i = 0; i < names.size(); ++i)
                (names.get(i).second == 0 ? other_currencies : rated_currencies).
                        put(names.get(i).first, new Currency(names.get(i).first, names.get(i).second));
        }
    }

    public Float getCurrencyRate(String base, String date, String toCompare) {
        synchronized (CurrenciesSingletone.class) {
            return (other_currencies.containsKey(base) ? other_currencies : rated_currencies).get(base).getRate(date, toCompare);
        }
    }

    public Currency getCurrencyRates(String base) {
        synchronized (CurrenciesSingletone.class) {
            return (other_currencies.containsKey(base) ? other_currencies : rated_currencies).get(base);
        }
    }

    public boolean hasInfo(String base, String date, String toCompare) {
        synchronized (CurrenciesSingletone.class) {
            return (other_currencies.containsKey(base) ? other_currencies : rated_currencies).get(base).hasInfo(date, toCompare);
        }
    }

    public boolean hasInfo(String name) {
        synchronized (CurrenciesSingletone.class) {
            return latestFeaturedDate.containsKey(name);
        }
    }

    public android.util.Pair<String, Integer>[] getCurrenciesNamesAndStates(boolean ratedOnly) {
        synchronized (CurrenciesSingletone.class) {
            android.util.Pair<String, Integer>[] ans = new android.util.Pair[rated_currencies.size() + (ratedOnly ? 0 :other_currencies.size())];
            int i = 0;
            for (Currency cur : rated_currencies.values())
                ans[i++] = new android.util.Pair<>(cur.getName(), cur.getState());

            if (!ratedOnly)
                for (Currency cur : other_currencies.values())
                    ans[i++] = new android.util.Pair<>(cur.getName(), cur.getState());

            return ans;
        }
    }

    public String[] getCurrenciesNames(boolean ratedOnly) {
        android.util.Pair<String, Integer>[] ans1 = getCurrenciesNamesAndStates(ratedOnly);
        String[] ans = new String[ans1.length];
        for (int i = 0; i < ans1.length; ++i)
            ans[i] = ans1[i].first;
        return ans;
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
        if (other_currencies.containsKey(s)) {
            other_currencies.get(s).changeState();
            rated_currencies.put(s, other_currencies.get(s));
            other_currencies.remove(s);
        }
        else {
            rated_currencies.get(s).changeState();
            other_currencies.put(s, rated_currencies.get(s));
            rated_currencies.remove(s);
        }
    }

}
