package ru.mail.android.androidmailproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;

import ru.mail.android.androidmailproject.adapters.MyAdapter;
import ru.mail.android.androidmailproject.adapters.RecyclerItemClickListener;
import ru.mail.android.androidmailproject.data.CurrenciesSingletone;

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
