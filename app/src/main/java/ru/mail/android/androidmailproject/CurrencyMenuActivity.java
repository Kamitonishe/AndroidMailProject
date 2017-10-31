package ru.mail.android.androidmailproject;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import ru.mail.android.androidmailproject.JsonModels.Currencies;
import ru.mail.android.androidmailproject.adapters.MyAdapter;
import ru.mail.android.androidmailproject.dataSingltones.CurrenciesSingletone;

/**
 * Created by dmitrykamaldinov on 10/31/17.
 */

public class CurrencyMenuActivity extends AppCompatActivity {
    private TextView textView;
    private MyAdapter recyclerAdapter;
    private RecyclerView recycleView;

    private String baseCurrencyName;
    private String currencyToCompare = "EUR";

    private GraphFragment graphFragment;
    private LoadingInCurrencyMenuFragment loadingFragment;
    FragmentTransaction fTrans;


    protected void recyclerViewSet(String[] s) {
        recycleView = (RecyclerView) findViewById(R.id.recycler1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycleView.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new MyAdapter(s);
        recycleView.setAdapter(recyclerAdapter);
    }

    public class JSONTaskForCurrencyMenu extends JSONTask {

        @Override
        protected void onPostExecute(Currencies[] result) {
            super.onPostExecute(result);
            fTrans = getFragmentManager().beginTransaction();
            fTrans.remove(loadingFragment);
            fTrans.add(R.id.fragmentsFrame, graphFragment);
            fTrans.commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_menu_activity);

        textView = (TextView)findViewById(R.id.textView);
        baseCurrencyName = getIntent().getStringExtra("currency_name");
        graphFragment = new GraphFragment();
        loadingFragment = new LoadingInCurrencyMenuFragment();

        Bundle toGraphFragment = new Bundle();
        toGraphFragment.putString("base_currency", baseCurrencyName);
        toGraphFragment.putString("currency_to_compare", currencyToCompare);
        graphFragment.setArguments(toGraphFragment);

        textView.setText(baseCurrencyName);

        recyclerViewSet(CurrenciesSingletone.getInstance().getCurrenciesNames());

        fTrans = getFragmentManager().beginTransaction();
        fTrans.add(R.id.fragmentsFrame, loadingFragment);
        fTrans.commit();

        try {
            showComparisionWithAnotherCurrency("EUR");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void showComparisionWithAnotherCurrency(String currency) throws ExecutionException, InterruptedException {
        currencyToCompare = currency;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());

        String[] params = new String[6];
        for (int i = 0; i < 3; ++i) {
            params[i * 2] = baseCurrencyName;
            params[2 * i + 1] = currentDate;
            currentDate = Helper.aMonthBefore(currentDate);
        }

        new JSONTaskForCurrencyMenu().execute(params);

        textView.setText(baseCurrencyName + " vs " + currencyToCompare);
    }
}
