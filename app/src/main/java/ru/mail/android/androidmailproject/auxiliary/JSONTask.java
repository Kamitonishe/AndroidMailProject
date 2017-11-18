package ru.mail.android.androidmailproject.auxiliary;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ru.mail.android.androidmailproject.JsonModels.Currencies;
import ru.mail.android.androidmailproject.data.CurrenciesSingletone;


/**
 * Created by dmitrykamaldinov on 10/30/17.
 */

public class JSONTask extends AsyncTask<String, String, Currencies[]>  {
    private String date = "2000-01-03", base = "EUR";
    private final String URL_HIT = "http://api.fixer.io/";

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected Currencies[] doInBackground(String... params) {
        Currencies[] result = new Currencies[params.length / 2];
        for (int i = 0; i < params.length / 2; ++i) {
            base = params[2 * i];
            date = params[2 * i + 1];
            String json = fromJSONtoString(URL_HIT + date + "?base=" + base);
            result[i] = getCurrencies(json);
            if (!date.equals("latest"))
                result[i].setDate(date);
            result[i].getRates().put(base, (float)1);
            CurrenciesSingletone.getInstance().addCurrency(result[i], date.equals("latest"));
        }
        return result;
    }

    @Override
    protected void onPostExecute(Currencies[] result) {
        super.onPostExecute(result);
    }

    public String fromJSONtoString(String urlHit) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        Currencies currencies = null;
        String finalJson = null;

        try {
            URL url = new URL(urlHit);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer  buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            finalJson = buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return finalJson;
    }

    public Currencies getCurrencies(String json) {
        Gson gson = new Gson();
        Currencies currencies = gson.fromJson(json, Currencies.class);
        return currencies;
    }


}
