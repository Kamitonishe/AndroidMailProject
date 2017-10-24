package ru.mail.android.androidmailproject;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Intent;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.zip.Inflater;

import ru.mail.android.androidmailproject.JsonModels.Currencies;
import ru.mail.android.androidmailproject.adapters.MyAdapter;

import android.util.Log;

/**
 * Created by dmitrykamaldinov on 10/24/17.
 */

public class SplashScreen extends AppCompatActivity {
    public static final String URL_HIT = "http://api.fixer.io/latest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        new JSONTask().execute();
    }

    private class JSONTask extends AsyncTask<String,String, Currencies> {


        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        //http://api.fixer.io/2000-01-03
        @Override
        protected Currencies doInBackground(String... params) {

            String json = fromJSONtoString(URL_HIT);
            return getCurrencies(json);
        }

        @Override
        protected void onPostExecute(Currencies result) {
            super.onPostExecute(result);

            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            intent.putExtra("ALL_CURRENCIES", getAllCurrencies(result));
            startActivity(intent);

            finish();

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

        public String[] getAllCurrencies(Currencies currencies) {
            Map<String, Float> map = currencies.getRates();
            int i = 0;
            String[] allCurrencies = new String[map.size() + 1];
            allCurrencies[0] = currencies.getBase();
            for(Map.Entry entry: map.entrySet()) {
                i++;
                allCurrencies[i] = (String)entry.getKey();
            }
            return allCurrencies;
        }
    }
}
