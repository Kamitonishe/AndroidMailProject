package ru.mail.android.androidmailproject.activities.mainActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.content.Intent;

import ru.mail.android.androidmailproject.activities.currencyMenuActivity.CurrencyMenuActivity;
import ru.mail.android.androidmailproject.R;
import ru.mail.android.androidmailproject.adapters.MyAdapter;
import ru.mail.android.androidmailproject.data.CurrenciesSingletone;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recycleView;

    protected void recyclerViewSet(Pair<String, Integer>[] namesAndStates) {
        recycleView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycleView.setLayoutManager(linearLayoutManager);
        MyAdapter recyclerAdapter = new MyAdapter(namesAndStates, this);
        recycleView.setAdapter(recyclerAdapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewSet(CurrenciesSingletone.getInstance().getCurrenciesNamesAndStates());
    }

    public void startCurrencyMenuActivity(String currency) {
        Intent intent = new Intent(MainActivity.this, CurrencyMenuActivity.class);
        intent.putExtra("currency_name", currency);
        startActivity(intent);

        onPause();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerViewSet(CurrenciesSingletone.getInstance().getCurrenciesNamesAndStates());
    }
}
