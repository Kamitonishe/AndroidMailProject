package ru.mail.android.androidmailproject.activities.mainActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Pair;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Map;

import ru.mail.android.androidmailproject.activities.currencyMenuActivity.CurrencyMenuActivity;
import ru.mail.android.androidmailproject.R;
import ru.mail.android.androidmailproject.adapters.MyAdapter;
import ru.mail.android.androidmailproject.auxiliary.ImageManager;
import ru.mail.android.androidmailproject.data.CurrenciesSingletone;
import ru.mail.android.androidmailproject.data.Currency;
import ru.mail.android.androidmailproject.sql.DBHelper;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recycleView;
    private DBHelper dbHelper;
    private FloatingActionButton options;
    private String toolbarImageName;

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
        final Context context = this;

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                final View v = factory.inflate(R.layout.options_dialog_layout, null);
                builder.setView(v);
                builder.setTitle("Options");

                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ListView lw = ((AlertDialog)dialogInterface).getListView();
                        Spinner pictureSpinner = v.findViewById(R.id.picture_spinner);
                        String[] array = getResources().getStringArray(R.array.pictures_array);
                        setToolbarImage(array[pictureSpinner.getSelectedItemPosition()]);
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                Dialog dialog = builder.create();
                dialog.show();

                final Spinner pictureSpinner = dialog.findViewById(R.id.picture_spinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                        R.array.pictures_array, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                pictureSpinner.setAdapter(adapter);

                builder.setItems(R.array.pictures_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
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

    private void setToolbarImage(String name) {
        int resourceId = getResources().getIdentifier(name.toLowerCase(), "drawable", getPackageName());
        ImageView image = (ImageView) findViewById(R.id.toolbarImage);
        image.setImageResource(resourceId);
    }
}
