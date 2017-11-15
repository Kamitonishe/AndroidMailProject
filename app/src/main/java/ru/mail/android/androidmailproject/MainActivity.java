package ru.mail.android.androidmailproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.content.Intent;

import java.util.Map;

import ru.mail.android.androidmailproject.adapters.MyAdapter;
import ru.mail.android.androidmailproject.data.CurrenciesSingletone;

public class MainActivity extends AppCompatActivity {
    private TextView textView;

    protected void recyclerViewSet(String[] s, Map<String, Integer> states) {
        MyAdapter recyclerAdapter;
        RecyclerView recycleView;
        LinearLayoutManager linearLayoutManager;
        recycleView = (RecyclerView) findViewById(R.id.recycler);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycleView.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new MyAdapter(s, states, this);
        recycleView.setAdapter(recyclerAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewSet(CurrenciesSingletone.getInstance().getCurrenciesNames(), CurrenciesSingletone.getInstance().getCurrenciesStates());

    }

    //при выборе валюты из recyclerview
    public void startCurrencyMenuActivity(String currency) {
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

    @Override
    protected void onResume() {
        super.onResume();
        recyclerViewSet(CurrenciesSingletone.getInstance().getCurrenciesNames(), CurrenciesSingletone.getInstance().getCurrenciesStates());
    }
}
