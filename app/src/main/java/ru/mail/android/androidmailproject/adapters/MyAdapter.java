package ru.mail.android.androidmailproject.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import ru.mail.android.androidmailproject.CurrencyMenuActivity;
import ru.mail.android.androidmailproject.MainActivity;
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
    private CurrencyMenuActivity currencyMenuActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public Button changeBtn;
        public RatingBar ratingBar;
        public ViewHolder(View v) {
            super(v);
            changeBtn = v.findViewById(R.id.chngBtn);
            mTextView = v.findViewById(R.id.textView);
            ratingBar = v.findViewById(R.id.ratingBar);
        }
    }

    public MyAdapter(String[] myDataset, Map<String, Integer> states, Context context) {
        mDataset = myDataset;
        mContext = context;
        helper = new DBHelper(context);
        this.states = states;
    }

    public void setCurrenciesActivity(CurrencyMenuActivity currenciesActivity) {
        this.currencyMenuActivity = currenciesActivity;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
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

                    SQLiteDatabase db = helper.getWritableDatabase();

                    db.execSQL("UPDATE currencies_names SET state = " + states.get(mDataset[position]) +
                            " WHERE name = \"" + mDataset[position] +"\"");
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
