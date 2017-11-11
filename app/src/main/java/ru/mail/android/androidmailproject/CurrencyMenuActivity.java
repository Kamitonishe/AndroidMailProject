package ru.mail.android.androidmailproject;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import ru.mail.android.androidmailproject.JsonModels.Currencies;
import ru.mail.android.androidmailproject.adapters.MyAdapter;
import ru.mail.android.androidmailproject.auxiliary.NetworkManager;
import ru.mail.android.androidmailproject.auxiliary.StringManager;
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


    protected void recyclerViewSet(String[] s, Map<String, Integer> states) {
        recycleView = (RecyclerView) findViewById(R.id.recycler1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycleView.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new MyAdapter(s, states, CurrencyMenuActivity.this);
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

        recyclerViewSet(CurrenciesSingletone.getInstance().getCurrenciesNames(), CurrenciesSingletone.getInstance().getCurrenciesStates());

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

        ArrayList<String> params = new ArrayList<>();

        if (NetworkManager.isNetworkAvailable(getApplicationContext())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = sdf.format(new Date());

            for (int i = 0; i < 4; ++i) {
                if (!CurrenciesSingletone.getInstance().hasInfo(baseCurrencyName, currentDate)) {
                    params.add(baseCurrencyName);
                    params.add(currentDate);
                }
                currentDate = StringManager.aMonthBefore(currentDate);
            }

        }

        new JSONTaskForCurrencyMenu().execute(params.toArray(new String[params.size()]));

        textView.setText(baseCurrencyName + " vs " + currencyToCompare);
    }
}
