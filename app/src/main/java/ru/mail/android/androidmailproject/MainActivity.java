package ru.mail.android.androidmailproject;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import ru.mail.android.androidmailproject.adapters.RecyclerItemClickListener;
import ru.mail.android.androidmailproject.dataSingltones.CurrenciesSingletone;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private MyAdapter recyclerAdapter;
    private RecyclerView recycleView;
    private LinearLayoutManager linearLayoutManager;

    protected void recyclerViewSet(String[] s) {
        recycleView = (RecyclerView) findViewById(R.id.recycler);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycleView.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new MyAdapter(s);
        recycleView.setAdapter(recyclerAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewSet(CurrenciesSingletone.getInstance().getCurrenciesNames());

        recycleView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recycleView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startCurrencyMenuActivity(((TextView)view.findViewById(R.id.textView)).getText().toString());
            }

            @Override
            public void onLongItemClick(View view, int position) {
                // do whatever
            }
        }));
    }

    //при выборе валюты из recyclerview
    private void startCurrencyMenuActivity(String currency) {
        Intent intent = new Intent(MainActivity.this, CurrencyMenuActivity.class);
        intent.putExtra("currency_name", currency);
        startActivity(intent);

        //это не должно завершаться
        onPause();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
