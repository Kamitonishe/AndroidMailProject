package ru.mail.android.androidmailproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import ru.mail.android.androidmailproject.adapters.MyAdapter;
import ru.mail.android.androidmailproject.dataSingltones.CurrenciesSingletone;

/**
 * Created by dmitrykamaldinov on 10/29/17.
 */

public class GraphicsActivity extends AppCompatActivity {

    private GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grap);
    }
}
