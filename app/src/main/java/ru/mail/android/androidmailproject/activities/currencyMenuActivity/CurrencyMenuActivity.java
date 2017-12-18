package ru.mail.android.androidmailproject.activities.currencyMenuActivity;

import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ru.mail.android.androidmailproject.activities.mainActivity.MainActivity;
import ru.mail.android.androidmailproject.auxiliary.AnimationManager;
import ru.mail.android.androidmailproject.auxiliary.CurrencyManager;
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
    private TextView textViewHelp;
    private FrameLayout fragmentsFrame;

    private String baseCurrencyName;
    private String currencyToCompare = "EUR";
    String lastDate;

    private GraphFragment graphFragment;
    private LoadingInCurrencyMenuFragment loadingFragment;
    private ChooseCurrencyToCompareFragment chooseFragment;
    FragmentTransaction fTrans;

    protected void recyclerViewSet() {
        RecyclerView recycleView = (RecyclerView) findViewById(R.id.recycler1);
        recycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycleView.setAdapter(new MyAdapter(CurrencyMenuActivity.this));
    }

    public void toggleContents(View v) throws InterruptedException {
        if(textViewHelp.isShown()) {
            AnimationManager.slide_up(this, textViewHelp);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewHelp.setVisibility(View.GONE);
                            fragmentsFrame.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }).start();
        }
        else {
            textViewHelp.setVisibility(View.VISIBLE);
            fragmentsFrame.setVisibility(View.GONE);
            AnimationManager.slide_down(this, textViewHelp);
        }
    }

    public String getLastDate() {
        return lastDate;
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

    JSONTaskForCurrencyMenu task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ?
                       R.layout.currency_menu_activity : R.layout.currency_menu_activity_landscape);
        recyclerViewSet();

        textView = (TextView)findViewById(R.id.textView);
        textViewHelp = (TextView)findViewById(R.id.textViewHelp);
        textViewHelp.setVisibility(View.GONE);
        baseCurrencyName = getIntent().getStringExtra("currency_name");
        graphFragment = new GraphFragment();
        loadingFragment = new LoadingInCurrencyMenuFragment();
        chooseFragment = new ChooseCurrencyToCompareFragment();
        fragmentsFrame = (FrameLayout)findViewById(R.id.fragmentsFrame);

        textView.setText(baseCurrencyName);
        textView.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/libduas.ttf"));
        textViewHelp.setText(CurrencyManager.getCurrencyInformation(baseCurrencyName));

        task = new JSONTaskForCurrencyMenu();

        fTrans = getFragmentManager().beginTransaction();
        fTrans.add(R.id.fragmentsFrame, chooseFragment);
        fTrans.commit();


        final int NOTIFICATION_ID = 1;

        PendingIntent activityPendingIntent = getActivityPendingIntent();

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Базовый курс")
                .setContentText(baseCurrencyName)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(activityPendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setCategory(Notification.CATEGORY_STATUS)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, notification);


    }
    private PendingIntent getActivityPendingIntent() {
        Intent activityIntent = new Intent(this, MainActivity.class);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    private void loadInformation() {
        ArrayList<String> params = new ArrayList<>();

        if (NetworkManager.isNetworkAvailable(getApplicationContext())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = lastDate == null ? sdf.format(new Date()) : lastDate;

            for (int i = 0; i < 4; ++i) {
                if (!CurrenciesSingletone.getInstance().hasInfo(baseCurrencyName, currentDate, currencyToCompare)) {
                    params.add(baseCurrencyName);
                    params.add(currentDate);
                }
                currentDate = DateManager.aMonthBefore(currentDate);
            }

        }

        task = new JSONTaskForCurrencyMenu();
        task.execute(params.toArray(new String[params.size()]));
    }

    public void showComparisionWithAnotherCurrency(String currency, String lastDate) {

        currencyToCompare = currency;
        this.lastDate = lastDate;

        Bundle toGraphFragment = new Bundle();
        toGraphFragment.putString("base_currency", baseCurrencyName);
        toGraphFragment.putString("currency_to_compare", currencyToCompare);
        toGraphFragment.putString("last_date", lastDate);

        if (graphFragment.getArguments() == null)
            graphFragment.setArguments(toGraphFragment);
        else
            graphFragment.getArguments().putAll(toGraphFragment);

        fTrans = getFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragmentsFrame, loadingFragment);
        fTrans.commit();

        textView.setText(baseCurrencyName + " vs " + currencyToCompare);

        loadInformation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        task.cancel(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (task.isCancelled())
            loadInformation();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.currency_menu_activity_landscape);
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
            setContentView((R.layout.currency_menu_activity));
    }
}
