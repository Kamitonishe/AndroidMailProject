package ru.mail.android.androidmailproject.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ru.mail.android.androidmailproject.CurrencyMenuActivity;
import ru.mail.android.androidmailproject.MainActivity;
import ru.mail.android.androidmailproject.R;

/**
 * Created by Franck on 23.10.2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private String[] mDataset;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public Button changeBtn;
        public ViewHolder(View v) {
            super(v);
            changeBtn = v.findViewById(R.id.chngBtn);
            mTextView = v.findViewById(R.id.textView);
        }
    }

    public MyAdapter(String[] myDataset, Context context) {
        mDataset = myDataset;
        mContext = context;
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
        holder.changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof MainActivity)
                    ((MainActivity)mContext).startCurrencyMenuActivity(mDataset[position]);
                else if (mContext instanceof CurrencyMenuActivity)
                    ((CurrencyMenuActivity)mContext).showComparisionWithAnotherCurrency(mDataset[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
