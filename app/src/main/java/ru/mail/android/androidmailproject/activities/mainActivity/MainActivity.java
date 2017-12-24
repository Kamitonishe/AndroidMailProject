package ru.mail.android.androidmailproject.activities.mainActivity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Pair;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import ru.mail.android.androidmailproject.JsonModels.Currencies;
import ru.mail.android.androidmailproject.activities.currencyMenuActivity.CurrencyMenuActivity;
import ru.mail.android.androidmailproject.R;
import ru.mail.android.androidmailproject.adapters.MyAdapter;
import ru.mail.android.androidmailproject.auxiliary.CustomRecyclerView;
import ru.mail.android.androidmailproject.auxiliary.ImageManager;
import ru.mail.android.androidmailproject.auxiliary.JSONTask;
import ru.mail.android.androidmailproject.data.CurrenciesSingletone;
import ru.mail.android.androidmailproject.data.Currency;
import ru.mail.android.androidmailproject.data.ImagesSingltone;
import ru.mail.android.androidmailproject.data.SuperSingltone;
import ru.mail.android.androidmailproject.notifications.TimeNotification;
import ru.mail.android.androidmailproject.sql.DBHelper;

public class MainActivity extends AppCompatActivity {
    public static final int NOTIFY_ID = 101;
    private DBHelper dbHelper;
    private String notificationString;

    protected void recyclerViewSet() {
        CustomRecyclerView recycleView = (CustomRecyclerView) findViewById(R.id.recycler);
        recycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycleView.setAdapter(new MyAdapter(this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        setToolbarImage(SuperSingltone.getInstance().getPicture());

        dbHelper = new DBHelper(getApplicationContext());
        final Context context = this;



        findViewById(R.id.options_fb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SuperSingltone.getInstance().setOptionsDialogIsCalled(true);
                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                final View v = factory.inflate(R.layout.options_dialog_layout, null);
                ((AppCompatCheckBox)v.findViewById(R.id.onlyFavoritesCB)).setChecked(SuperSingltone.getInstance().isOnlyFavorites());
                ((AppCompatCheckBox)v.findViewById(R.id.compareOnlyToFavoritesCB)).setChecked(SuperSingltone.getInstance().isCompareOnlyToFavorites());

                builder.setView(v);
                //builder.setTitle("Options");

                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ListView lw = ((AlertDialog)dialogInterface).getListView();
                        Spinner pictureSpinner = v.findViewById(R.id.picture_spinner);
                        String[] array = getResources().getStringArray(R.array.pictures_array);
                        setToolbarImage(array[pictureSpinner.getSelectedItemPosition()]);
                        SuperSingltone.getInstance().setOptionsDialogIsCalled(false);
                        SuperSingltone.getInstance().setOnlyFavorites(((AppCompatCheckBox)v.findViewById(R.id.onlyFavoritesCB)).isChecked());
                        SuperSingltone.getInstance().setCompareOnlyToFavorites(((AppCompatCheckBox)v.findViewById(R.id.compareOnlyToFavoritesCB)).isChecked());
                        recyclerViewSet();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SuperSingltone.getInstance().setOptionsDialogIsCalled(false);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.setTitle("OPTIONS");

                final Spinner pictureSpinner = (Spinner)dialog.findViewById(R.id.picture_spinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                        R.array.pictures_array, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                pictureSpinner.setAdapter(adapter);
                String tmp = SuperSingltone.getInstance().getPicture();
                pictureSpinner.setSelection(adapter.getPosition(tmp.substring(0, 1).toUpperCase() + tmp.substring(1).toLowerCase()));

                Button wipeData = (Button) dialog.findViewById(R.id.wipeButton);
                wipeData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                db.execSQL("DELETE FROM currencies_rates WHERE rowid >= 0");
                            }
                        }).start();
                    }
                });

                builder.setItems(R.array.pictures_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            }
        });

        recyclerViewSet();

        if (SuperSingltone.getInstance().isOptionsDialogCalled())
            findViewById(R.id.options_fb).callOnClick();


        scheduleNotification();
/*
        final int NOTIFICATION_ID = 1;

        PendingIntent activityPendingIntent = getActivityPendingIntent();

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Избранные курсы")
                .setContentText("eg")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(activityPendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setCategory(Notification.CATEGORY_STATUS)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, notification);*/
    }

    private PendingIntent getActivityPendingIntent() {
        Intent activityIntent = new Intent(this, MainActivity.class);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("INSERT OR REPLACE INTO whichCurrencies(rowid, only) VALUES(0, " + (SuperSingltone.getInstance().isOnlyFavorites() ? 1 : 0) + ")");
                db.execSQL("INSERT OR REPLACE INTO whichCurrencies(rowid, only) VALUES(1, " + (SuperSingltone.getInstance().isCompareOnlyToFavorites() ? 1 : 0) + ")");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("INSERT OR REPLACE INTO picture(rowid, name) VALUES(0, \"" + SuperSingltone.getInstance().getPicture().toLowerCase() + "\")");
            }
        }).start();

        for (final Pair<String, Integer> nameAndState : CurrenciesSingletone.getInstance().getCurrenciesNamesAndStates(false))
            if (nameAndState.second == 1)
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        Map<Pair<String, String>, Float> cur = CurrenciesSingletone.getInstance().getCurrencyRates(nameAndState.first).getRates();
                        for (Map.Entry<Pair<String, String>, Float> entry : cur.entrySet()) {

                            String rowid = "(SELECT rowid FROM currencies_rates WHERE base = \"" + nameAndState.first + "\" and date = \"" +
                                entry.getKey().first + "\" and toCompare = \"" + entry.getKey().second + "\")";

                            db.execSQL("INSERT OR REPLACE INTO currencies_rates(rowid, base, date, toCompare, rate) VALUES(" + rowid +", \"" +
                                    nameAndState.first + "\", \"" + entry.getKey().first + "\", \"" + entry.getKey().second + "\", " +
                                    String.valueOf(entry.getValue()) + ")");
                        }
                    }
                }).start();
        super.onStop();
    }

    private void setToolbarImage(String name) {
        int resourceId = getResources().getIdentifier(name.toLowerCase(), "drawable", getPackageName());
        ImageView image = (ImageView) findViewById(R.id.toolbarImage);
        image.setImageResource(resourceId);
        SuperSingltone.getInstance().setPicture(name);
    }

    private void scheduleNotification() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        new JSONTaskForNotification().execute("RUB", sdf.format(new Date()));



    }

    private class JSONTaskForNotification extends JSONTask {
        @Override
        protected void onPostExecute(Currencies[] result) {
            if (!isCancelled()) {
                notificationString = "";
                Currency cur = CurrenciesSingletone.getInstance().getCurrencyRates(base);
                Currencies new_cur = result[0];
                for (Map.Entry<String, Float> entry : new_cur.getRates().entrySet()) {
                    String currency = entry.getKey();
                    if (!currency.equals(base) && CurrenciesSingletone.getInstance().isRated(currency)) {
                        notificationString += (base + " - " + currency + ": " +
                                (cur.getLast() == null ? "undefined" :
                                        cur.getRates().get(new Pair<>(cur.getLast(), currency)) + " (" + cur.getLast() + ") ")
                                + "->\n             " + entry.getValue() + "(" + new_cur.getDate() + ")\n\n");
                    }
                }
            }

            super.onPostExecute(result);

            Notification notification = new NotificationCompat.Builder(MainActivity.this)
                    .setContentTitle("Курс валют (RUB)")
                    .setSmallIcon(R.mipmap.icon)
                    .setContentIntent(getActivityPendingIntent())
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setCategory(Notification.CATEGORY_STATUS)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(notificationString))
                    .build();

            Intent notificationIntent = new Intent(MainActivity.this, TimeNotification.class);
            notificationIntent.putExtra(TimeNotification.NOTIFICATION_ID, 1);
            notificationIntent.putExtra(TimeNotification.NOTIFICATION, notification);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 1000,
                    1000, pendingIntent);

            /*
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 12);

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);*/
        }
    }

}
