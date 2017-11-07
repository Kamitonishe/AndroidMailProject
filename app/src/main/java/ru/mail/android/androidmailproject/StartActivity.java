package ru.mail.android.androidmailproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

import ru.mail.android.androidmailproject.JsonModels.Currencies;
import ru.mail.android.androidmailproject.data.CurrenciesSingletone;

/**
 * Created by dmitrykamaldinov on 10/24/17.
 */

public class StartActivity extends AppCompatActivity {
    public class JSONTaskInStart extends JSONTask {

        protected void onPostExecute(Currencies[] result) {
            super.onPostExecute(result);
            CurrenciesSingletone.getInstance().fillCurrenciesNames(result[0]);
            callMainActivity();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        new JSONTaskInStart().execute("RUB", "latest");
    }

    public void callMainActivity() {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
        StartActivity.this.finish();
    }
}
