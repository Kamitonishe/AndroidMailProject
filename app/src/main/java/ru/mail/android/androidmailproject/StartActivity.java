package ru.mail.android.androidmailproject;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import ru.mail.android.androidmailproject.JsonModels.Currencies;
import ru.mail.android.androidmailproject.dataSingltones.CurrenciesSingletone;

/**
 * Created by dmitrykamaldinov on 10/24/17.
 */

public class StartActivity extends AppCompatActivity {
    public class JSONTaskInStart extends JSONTask {

        protected void onPostExecute(Currencies result) {
            super.onPostExecute(result);
            callMainActivity(result);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        new JSONTaskInStart().execute("2017-01-03", "RUB");
    }

    public void callMainActivity(Currencies result) {
        CurrenciesSingletone.getInstance().setCurrencies(result);
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
        StartActivity.this.finish();

    }
}
