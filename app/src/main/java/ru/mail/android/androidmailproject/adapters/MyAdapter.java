package ru.mail.android.androidmailproject.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mail.android.androidmailproject.activities.currencyMenuActivity.CurrencyMenuActivity;
import ru.mail.android.androidmailproject.activities.mainActivity.MainActivity;
import ru.mail.android.androidmailproject.R;
import ru.mail.android.androidmailproject.data.CurrenciesSingletone;
import ru.mail.android.androidmailproject.data.ImagesSingltone;
import ru.mail.android.androidmailproject.sql.DBHelper;


/**
 * Created by Franck on 23.10.2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private String[] mCurrencyNamesSet;
    private Context context;
    private Map<String, Integer> states;
    private DBHelper dbHelper;
    private ExecutorService service;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        Button changeBtn;
        RatingBar ratingBar;
        ImageView imageView;

        ViewHolder(View v) {
            super(v);
            changeBtn = v.findViewById(R.id.chngBtn);
            textView = v.findViewById(R.id.textView);
            ratingBar = v.findViewById(R.id.ratingBar);
            imageView = v.findViewById(R.id.imageView);
        }
    }

    public MyAdapter(Context context) {
        Pair<String, Integer>[] currencies = CurrenciesSingletone.getInstance().getCurrenciesNamesAndStates();
        int i = 0;
        this.context = context;
        dbHelper = new DBHelper(context);
        mCurrencyNamesSet = new String[currencies.length];
        states = new HashMap<>();
        service = Executors.newCachedThreadPool();

<<<<<<< HEAD
=======
        for (Pair<String, Integer> nameAndState : currencies) {
            mCurrencyNamesSet[i++] = nameAndState.first;
            this.states.put(nameAndState.first, nameAndState.second);
        }
    }
>>>>>>> 905c261ff825dd62422134a576a67947d746f45b

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textView.setText(mCurrencyNamesSet[position]);
        holder.textView.setTypeface(Typeface.createFromAsset(
               context.getAssets() , "fonts/libduas.ttf"));


        holder.ratingBar.setRating(states.get(mCurrencyNamesSet[position]));
        holder.changeBtn.setOnClickListener(new ChangeBtnListener(position));
        holder.ratingBar.setOnTouchListener(new RatingBarListener(position));
        ImagesSingltone.getInstance().setContext(context);
        ImagesSingltone.getInstance().loadImage(context, position, holder.imageView);
    }


    private class ChangeBtnListener implements View.OnClickListener {

        private int position;
        ChangeBtnListener(int position){
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            if (context instanceof MainActivity)
                ((MainActivity) context).startCurrencyMenuActivity(mCurrencyNamesSet[position]);
            else if (context instanceof CurrencyMenuActivity)
                ((CurrencyMenuActivity) context).showComparisionWithAnotherCurrency(mCurrencyNamesSet[position]);
        }
    }

    private class RatingBarListener implements View.OnTouchListener {

        private int position;
        RatingBarListener(int position){
            this.position = position;
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                ((RatingBar)view).setRating(1 - ((RatingBar)view).getRating());
                states.put(mCurrencyNamesSet[position], (int)(1 - ((RatingBar)view).getRating()));
                CurrenciesSingletone.getInstance().changeState(mCurrencyNamesSet[position]);

                service.submit(new Runnable() {
                    @Override
                    public void run() {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        db.execSQL("UPDATE currencies_names SET state = " + (1 - states.get(mCurrencyNamesSet[position])) +
                                " WHERE name = \"" + mCurrencyNamesSet[position] +"\"");
                    }
                });
            }
            return true;
        }
    }


    @Override
    public int getItemCount() {
        return mCurrencyNamesSet.length;
    }
}
