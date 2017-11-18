package ru.mail.android.androidmailproject.activities.StartActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.mail.android.androidmailproject.R;

/**
 * Created by dmitrykamaldinov on 11/18/17.
 */

public class NoConnectionFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.no_connection_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button retryButton = getView().findViewById(R.id.retryButton);

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((StartActivity)getActivity()).tryToCallMainActivity();
            }
        });
    }
}
