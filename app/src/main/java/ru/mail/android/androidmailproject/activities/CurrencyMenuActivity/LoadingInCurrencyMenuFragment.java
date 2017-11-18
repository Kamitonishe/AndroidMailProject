package ru.mail.android.androidmailproject.activities.CurrencyMenuActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.mail.android.androidmailproject.R;

/**
 * Created by dmitrykamaldinov on 10/31/17.
 */

public class LoadingInCurrencyMenuFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.loading_fragment_layout, null);
    }
}
