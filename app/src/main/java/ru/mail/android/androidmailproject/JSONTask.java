package ru.mail.android.androidmailproject;

import android.app.ProgressDialog;
import android.content.Intent;
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
import ru.mail.android.androidmailproject.dataSingltones.CurrenciesSingletone;

/**
 * Created by dmitrykamaldinov on 10/30/17.
 */

public class JSONTask extends AsyncTask<String,String, Currencies>  {
    private String date = "2000-01-03", base = "EUR";
    private final String URL_HIT = "http://api.fixer.io/";

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected Currencies doInBackground(String... params) {
        date = params[0];
        base = params[1];
        String json = fromJSONtoString(URL_HIT + date + "?base=" + base);
        return getCurrencies(json);
    }

    @Override
    protected void onPostExecute(Currencies result) {
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
