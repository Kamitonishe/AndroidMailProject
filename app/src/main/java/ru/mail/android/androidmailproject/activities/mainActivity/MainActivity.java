package ru.mail.android.androidmailproject.activities.mainActivity;

import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import java.util.Map;

import ru.mail.android.androidmailproject.activities.currencyMenuActivity.CurrencyMenuActivity;
import ru.mail.android.androidmailproject.R;
import ru.mail.android.androidmailproject.adapters.MyAdapter;
import ru.mail.android.androidmailproject.data.CurrenciesSingletone;
import ru.mail.android.androidmailproject.data.Currency;
import ru.mail.android.androidmailproject.sql.DBHelper;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recycleView;
    private DBHelper dbHelper;
    private FloatingActionButton options;

    protected void recyclerViewSet() {
        recycleView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycleView.setLayoutManager(linearLayoutManager);
        MyAdapter recyclerAdapter = new MyAdapter(this);
        recycleView.setAdapter(recyclerAdapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(getApplicationContext());
        options = (FloatingActionButton) findViewById(R.id.options_fb);

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "KEK", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerViewSet();
    }

    public void startCurrencyMenuActivity(String currency) {
        Intent intent = new Intent(MainActivity.this, CurrencyMenuActivity.class);
        intent.putExtra("currency_name", currency);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerViewSet();
    }

    @Override
    protected void onStop() {
        for (final Pair<String, Integer> nameAndState : CurrenciesSingletone.getInstance().getCurrenciesNamesAndStates())
            if (nameAndState.second == 1)
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        Map<Pair<String, String>, Float> cur = CurrenciesSingletone.getInstance().getCurrencyRates(nameAndState.first).getRates();
                        for (Map.Entry<Pair<String, String>, Float> entry : cur.entrySet()) {
                            db.execSQL("INSERT OR REPLACE INTO currencies_rates(base, date, toCompare, rate) VALUES(\"" +
                                    nameAndState.first + "\", \"" + entry.getKey().first + "\", \"" + entry.getKey().second + "\", " +
                                    String.valueOf(entry.getValue()) + ")");
                        }
                    }
                }).start();
        super.onStop();
    }
}
