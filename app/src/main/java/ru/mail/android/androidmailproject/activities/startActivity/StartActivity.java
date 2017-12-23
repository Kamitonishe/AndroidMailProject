package ru.mail.android.androidmailproject.activities.startActivity;

import android.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mail.android.androidmailproject.auxiliary.JSONTask;
import ru.mail.android.androidmailproject.JsonModels.Currencies;
import ru.mail.android.androidmailproject.activities.mainActivity.MainActivity;
import ru.mail.android.androidmailproject.R;
import ru.mail.android.androidmailproject.auxiliary.NetworkManager;
import ru.mail.android.androidmailproject.data.CurrenciesSingletone;
import ru.mail.android.androidmailproject.data.ImagesSingltone;
import ru.mail.android.androidmailproject.data.SuperSingltone;
import ru.mail.android.androidmailproject.sql.DBHelper;

/**
 * Created by dmitrykamaldinov on 10/24/17.
 */

public class StartActivity extends AppCompatActivity {
    private DBHelper helper;
    private ExecutorService service;

    private NoConnectionFragment noConnectionFragment;
    private ProgressBarFragment progressBarFragment;


    public class JSONTaskInStart extends JSONTask {

        protected void onPostExecute(Currencies[] result) {
            super.onPostExecute(result);
            CurrenciesSingletone.getInstance().fillCurrencies(result[0]);

            String query = "INSERT INTO currencies_names(name, state) VALUES (\"" + result[0].getBase() + "\", 0)";

            for (Map.Entry entry : result[0].getRates().entrySet())
                query += ", (\"" + (String) entry.getKey() + "\", 0)";

            final String query_ = query;

            service.submit(new Runnable() {
                @Override
                public void run() {
                    SQLiteDatabase db_write = helper.getWritableDatabase();
                    db_write.execSQL(query_);
                }
            });

            callMainActivity();
        }
    }

    void tryToCallMainActivity() {
        service.submit(new Runnable() {
            @Override
            public void run() {
                CurrenciesSingletone.getInstance().setContext(getApplicationContext());
                CurrenciesSingletone.getInstance().loadCurrencyInfo();
            }
        });
        service.submit(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = helper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT count(*) FROM picture", null);
                cursor.moveToFirst();
                if (cursor.getInt(0) == 0)
                    SuperSingltone.getInstance().setPicture("sea");
                else {
                    cursor = db.rawQuery("SELECT * FROM picture", null);
                    cursor.moveToFirst();
                    SuperSingltone.getInstance().setPicture(cursor.getString(0));
                }
            }
        });
        service.submit(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor cursor = db.rawQuery("SELECT count(*) FROM whichCurrencies", null);
                cursor.moveToFirst();
                if (cursor.getInt(0) == 0) {
                    SuperSingltone.getInstance().setCompareOnlyToFavorites(false);
                    SuperSingltone.getInstance().setOnlyFavorites(false);
                }
                else {
                    cursor = db.rawQuery("SELECT * FROM whichCurrencies", null);
                    cursor.moveToFirst();
                    SuperSingltone.getInstance().setOnlyFavorites(cursor.getInt(0) == 1);
                    cursor.moveToNext();
                    SuperSingltone.getInstance().setCompareOnlyToFavorites(cursor.getInt(0) == 1);
                }
            }
        });
        service.submit(new Runnable() {
            @Override
            public void run() {
                String count = "SELECT count(*) FROM currencies_names";
                final SQLiteDatabase db = helper.getReadableDatabase();
                Cursor mcursor = db.rawQuery(count, null);
                mcursor.moveToFirst();

                if (mcursor.getInt(0) == 0) {
                    if (NetworkManager.isNetworkAvailable(getApplicationContext()))
                        new JSONTaskInStart().execute("RUB", "latest");
                    else {
                        Toast.makeText(getApplicationContext(),
                                "Need internet connection for the first run", Toast.LENGTH_LONG).show();

                        FragmentTransaction ftrans = getFragmentManager().beginTransaction();
                        ftrans.replace(R.id.fragmentsFrameStart, noConnectionFragment);
                        ftrans.commit();
                    }
                }
                else {
                    String query = "SELECT * FROM currencies_names";
                    mcursor.close();
                    mcursor = db.rawQuery(query, null);
                    mcursor.moveToFirst();
                    ArrayList<Pair<String, Integer>> names = new ArrayList<>();
                    do {
                        names.add(new Pair<>(mcursor.getString(0), mcursor.getInt(1)));
                    } while (mcursor.moveToNext());

                    CurrenciesSingletone.getInstance().fillCurrencies(names);

                    service.submit(new Runnable() {
                        @Override
                        public void run() {
                            Cursor cursor = db.rawQuery("SELECT * FROM currencies_rates", null);
                            cursor.moveToFirst();
                            do {
                                CurrenciesSingletone.getInstance().getCurrencyRates(cursor.getString(0))
                                        .getRates().put(new android.util.Pair<>(cursor.getString(1), cursor.getString(2)), cursor.getFloat(3));
                            } while (cursor.moveToNext());
                            cursor.close();
                        }
                    });

                    callMainActivity();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        helper = new DBHelper(this);
        service = Executors.newCachedThreadPool();
        progressBarFragment = new ProgressBarFragment();
        noConnectionFragment = new NoConnectionFragment();

        FragmentTransaction ftrans = getFragmentManager().beginTransaction();
        ftrans.add(R.id.fragmentsFrameStart, progressBarFragment);
        ftrans.commit();

        tryToCallMainActivity();
    }

    public void callMainActivity() {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
        StartActivity.this.finish();
    }
}