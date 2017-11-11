package ru.mail.android.androidmailproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

import java.io.BufferedReader;
import java.util.Map;

import ru.mail.android.androidmailproject.adapters.MyAdapter;
import ru.mail.android.androidmailproject.adapters.RecyclerItemClickListener;
import ru.mail.android.androidmailproject.data.CurrenciesSingletone;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private MyAdapter recyclerAdapter;
    private RecyclerView recycleView;
    private LinearLayoutManager linearLayoutManager;

    protected void recyclerViewSet(String[] s, Map<String, Integer> states) {
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



/*
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
*/
    }

    //при выборе валюты из recyclerview
    public void startCurrencyMenuActivity(String currency) {
        Intent intent = new Intent(MainActivity.this, CurrencyMenuActivity.class);
        intent.putExtra("currency_name", currency);
        startActivity(intent);

        //это не должно завершаться
        onPause();
    }
    public void startCurrencyChangeActivity(String currency) {
        Intent intent = new Intent(MainActivity.this, ChangeCurrencyActivity.class);
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
