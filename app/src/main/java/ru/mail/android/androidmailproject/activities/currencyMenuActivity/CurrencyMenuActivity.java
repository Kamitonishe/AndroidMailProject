package ru.mail.android.androidmailproject.activities.currencyMenuActivity;

import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ru.mail.android.androidmailproject.auxiliary.JSONTask;
import ru.mail.android.androidmailproject.JsonModels.Currencies;
import ru.mail.android.androidmailproject.R;
import ru.mail.android.androidmailproject.adapters.MyAdapter;
import ru.mail.android.androidmailproject.auxiliary.NetworkManager;
import ru.mail.android.androidmailproject.auxiliary.DateManager;
import ru.mail.android.androidmailproject.data.CurrenciesSingletone;

/**
 * Created by dmitrykamaldinov on 10/31/17.
 */

public class CurrencyMenuActivity extends AppCompatActivity {
    private TextView textView;
    private RecyclerView recycleView;

    private String baseCurrencyName;
    private String currencyToCompare = "EUR";

    private GraphFragment graphFragment;
    private LoadingInCurrencyMenuFragment loadingFragment;
    private ChooseCurrencyToCompareFragment chooseFragment;
    FragmentTransaction fTrans;

    protected void recyclerViewSet() {
        recycleView = (RecyclerView) findViewById(R.id.recycler1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycleView.setLayoutManager(linearLayoutManager);
        MyAdapter recyclerAdapter = new MyAdapter(CurrencyMenuActivity.this);
        recycleView.setAdapter(recyclerAdapter);
    }

    public class JSONTaskForCurrencyMenu extends JSONTask {

        @Override
        protected void onPostExecute(Currencies[] result) {
            super.onPostExecute(result);
            if (!isCancelled()) {
                fTrans = getFragmentManager().beginTransaction();
                fTrans.replace(R.id.fragmentsFrame, graphFragment);

                fTrans.commit();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_menu_activity);

        recyclerViewSet();

        textView = (TextView)findViewById(R.id.textView);
        baseCurrencyName = getIntent().getStringExtra("currency_name");
        graphFragment = new GraphFragment();
        loadingFragment = new LoadingInCurrencyMenuFragment();
        chooseFragment = new ChooseCurrencyToCompareFragment();

        textView.setText(baseCurrencyName);
        textView.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/libduas.ttf"));

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

        ArrayList<String> params = new ArrayList<>();

        if (NetworkManager.isNetworkAvailable(getApplicationContext())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = sdf.format(new Date());

            for (int i = 0; i < 4; ++i) {
                if (!CurrenciesSingletone.getInstance().hasInfo(baseCurrencyName, currentDate, currencyToCompare)) {
                    params.add(baseCurrencyName);
                    params.add(currentDate);
                }
                currentDate = DateManager.aMonthBefore(currentDate);
            }

        }

        new JSONTaskForCurrencyMenu().execute(params.toArray(new String[params.size()]));

        textView.setText(baseCurrencyName + " vs " + currencyToCompare);
    }
}
