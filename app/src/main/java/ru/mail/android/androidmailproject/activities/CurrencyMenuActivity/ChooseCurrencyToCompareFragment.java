package ru.mail.android.androidmailproject.activities.CurrencyMenuActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.mail.android.androidmailproject.R;

/**
 * Created by dmitrykamaldinov on 10/31/17.
 */

public class ChooseCurrencyToCompareFragment extends Fragment {
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.choose_currency_to_compare_layout, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = getView().findViewById(R.id.textView___);
        textView.setText("Выберите валюту для сравнения");
    }
}
