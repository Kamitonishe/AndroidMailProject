package ru.mail.android.androidmailproject.activities.startActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.mail.android.androidmailproject.R;

/**
 * Created by dmitrykamaldinov on 11/18/17.
 */

public class ProgressBarFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.progress_bar_fragment, null);
    }
}
