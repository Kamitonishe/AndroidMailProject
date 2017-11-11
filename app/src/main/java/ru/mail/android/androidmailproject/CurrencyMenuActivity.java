package ru.mail.android.androidmailproject;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.mail.android.androidmailproject.JsonModels.Currencies;
import ru.mail.android.androidmailproject.adapters.MyAdapter;
import ru.mail.android.androidmailproject.adapters.RecyclerItemClickListener;
import ru.mail.android.androidmailproject.data.CurrenciesSingletone;

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
    private ChooseCurrencyToCompareFragment chooseFragment;
    FragmentTransaction fTrans;


    protected void recyclerViewSet(String[] s) {
        recycleView = (RecyclerView) findViewById(R.id.recycler1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycleView.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new MyAdapter(s, CurrencyMenuActivity.this);
        recycleView.setAdapter(recyclerAdapter);

        /*
        recycleView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recycleView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showComparisionWithAnotherCurrency(((TextView)view.findViewById(R.id.textView)).getText().toString());
            }

            @Override
            public void onLongItemClick(View view, int position) {
                // do whatever
            }
        }));
        */
    }

    public class JSONTaskForCurrencyMenu extends JSONTask {

        @Override
        protected void onPostExecute(Currencies[] result) {
            super.onPostExecute(result);

            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.fragmentsFrame, graphFragment);

            fTrans.commit();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_menu_activity);

        recyclerViewSet(CurrenciesSingletone.getInstance().getCurrenciesNames());

        textView = (TextView)findViewById(R.id.textView);
        baseCurrencyName = getIntent().getStringExtra("currency_name");
        graphFragment = new GraphFragment();
        loadingFragment = new LoadingInCurrencyMenuFragment();
        chooseFragment = new ChooseCurrencyToCompareFragment();

        textView.setText(baseCurrencyName);

        fTrans = getFragmentManager().beginTransaction();
        fTrans.add(R.id.fragmentsFrame, chooseFragment);
        fTrans.commit();
    }

    public void showComparisionWithAnotherCurrency(String currency) {

        currencyToCompare = currency;

        Bundle toGraphFragment = new Bundle();
        toGraphFragment.putString("base_currency", baseCurrencyName);
        toGraphFragment.putString("currency_to_compare", currencyToCompare);
        graphFragment.setArguments(toGraphFragment);

        fTrans = getFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragmentsFrame, loadingFragment);
        fTrans.commit();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());

        String[] params = new String[8];
        for (int i = 0; i < 4; ++i) {
            params[i * 2] = baseCurrencyName;
            params[2 * i + 1] = currentDate;
            currentDate = Helper.aMonthBefore(currentDate);
        }

        new JSONTaskForCurrencyMenu().execute(params);

        textView.setText(baseCurrencyName + " vs " + currencyToCompare);
    }
}
