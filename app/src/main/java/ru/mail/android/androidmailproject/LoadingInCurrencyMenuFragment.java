package ru.mail.android.androidmailproject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;

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
