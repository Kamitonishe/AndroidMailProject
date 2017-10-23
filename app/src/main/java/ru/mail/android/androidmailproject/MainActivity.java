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

public class MainActivity extends AppCompatActivity {

    public static final String URL_HIT = "http://api.fixer.io/latest";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //https://www.youtube.com/watch?v=s8RGsjLmi24&t=193s
        new JSONTask().execute();
    }


    public class JSONTask extends AsyncTask<String,String, Currencies> {
        private TextView textView;
        private MyAdapter recyclerAdapter;
        private RecyclerView recycleView;
        private LinearLayoutManager linearLayoutManager;

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
            recyclerViewSet(getAllCurrencies(result));
        }

        public void recyclerViewSet(String[] s) {
            recycleView = (RecyclerView) findViewById(R.id.recycler);
            linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            recycleView.setLayoutManager(linearLayoutManager);
            recyclerAdapter = new MyAdapter(s);
            recycleView.setAdapter(recyclerAdapter);
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
