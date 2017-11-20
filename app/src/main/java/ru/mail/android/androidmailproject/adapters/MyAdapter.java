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
import ru.mail.android.androidmailproject.sql.DBHelper;


/**
 * Created by Franck on 23.10.2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private String[] mDataset;
    private Context mContext;
    private Map<String, Integer> states;
    private DBHelper helper;
    private ExecutorService service;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        Button changeBtn;
        RatingBar ratingBar;

        ViewHolder(View v) {
            super(v);
            changeBtn = v.findViewById(R.id.chngBtn);
            mTextView = v.findViewById(R.id.textView);
            ratingBar = v.findViewById(R.id.ratingBar);
        }
    }

    public MyAdapter(Pair<String, Integer>[] currencies, Context context) {
        int i = 0;
        mContext = context;
        helper = new DBHelper(context);
        mDataset = new String[currencies.length];
        states = new HashMap<>();
        service = Executors.newCachedThreadPool();

        for (Pair<String, Integer> nameAndState : currencies) {
            mDataset[i++] = nameAndState.first;
            this.states.put(nameAndState.first, nameAndState.second);
        }
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTextView.setText(mDataset[position]);
        holder.mTextView.setTypeface(Typeface.createFromAsset(
               mContext.getAssets() , "fonts/libduas.ttf"));

        holder.ratingBar.setRating(states.get(mDataset[position]));
        holder.changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof MainActivity)
                    ((MainActivity)mContext).startCurrencyMenuActivity(mDataset[position]);
                else if (mContext instanceof CurrencyMenuActivity)
                    ((CurrencyMenuActivity)mContext).showComparisionWithAnotherCurrency(mDataset[position]);
            }
        });

        holder.ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((RatingBar)view).setRating(1 - ((RatingBar)view).getRating());
                    states.put(mDataset[position], (int)(1 - ((RatingBar)view).getRating()));
                    CurrenciesSingletone.getInstance().changeState(mDataset[position]);

                    service.submit(new Runnable() {
                        @Override
                        public void run() {
                            SQLiteDatabase db = helper.getWritableDatabase();

                            db.execSQL("UPDATE currencies_names SET state = " + (1 - states.get(mDataset[position])) +
                                    " WHERE name = \"" + mDataset[position] +"\"");
                        }
                    });
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
