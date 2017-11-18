package ru.mail.android.androidmailproject.activities.MainActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.widget.TextView;
import android.content.Intent;

import ru.mail.android.androidmailproject.activities.CurrencyMenuActivity.CurrencyMenuActivity;
import ru.mail.android.androidmailproject.R;
import ru.mail.android.androidmailproject.adapters.MyAdapter;
import ru.mail.android.androidmailproject.data.CurrenciesSingletone;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private MyAdapter recyclerAdapter;
    private RecyclerView recycleView;
    private LinearLayoutManager linearLayoutManager;

    protected void recyclerViewSet(Pair<String, Integer>[] namesAndStates) {
        recycleView = (RecyclerView) findViewById(R.id.recycler);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycleView.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new MyAdapter(namesAndStates, this);
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
