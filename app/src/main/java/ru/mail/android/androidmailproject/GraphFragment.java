package ru.mail.android.androidmailproject;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ru.mail.android.androidmailproject.JsonModels.Currencies;
import ru.mail.android.androidmailproject.dataSingltones.CurrenciesSingletone;

/**
 * Created by dmitrykamaldinov on 10/31/17.
 */

public class GraphFragment extends Fragment {
    private String baseCurrency, currencyToCompare;
    private TextView latest;
    private GraphView graph;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseCurrency = getArguments().getString("base_currency");
        currencyToCompare = getArguments().getString("currency_to_compare");

        return inflater.inflate(R.layout.graph_fragment_layout, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        latest = (TextView)getView().findViewById(R.id.latest_textview);
        graph = (GraphView)getView().findViewById(R.id.graph_);

        initGraph();

        Currencies cur = CurrenciesSingletone.getInstance().getCurrencyInfo(baseCurrency, "latest");
        latest.setText("\n\nКурс на " + cur.getDate() + " : " + cur.getRates().get(currencyToCompare));
    }

    private void initGraph() {
        graph.getGridLabelRenderer().setHumanRounding(false);

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space
        graph.getViewport().setXAxisBoundsManual(true);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());

        DataPoint[] points = new DataPoint[3];
        for (int i = 0; i < 3; ++i) {
            String[] splited = currentDate.split("-");
            int y = new Integer(splited[0]);
            int m = new Integer(splited[1]);
            int d = new Integer(splited[2]);
            points[i] = new DataPoint(new Date(y, m - 1, d), CurrenciesSingletone.getInstance().getCurrencyInfo(baseCurrency, currentDate).getRates().get(currencyToCompare));
            currentDate = Helper.aMonthBefore(currentDate);
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);

        series.setTitle(baseCurrency + " in " + currencyToCompare);
        series.setColor(Color.RED);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(10);

        graph.addSeries(series);

        graph.getViewport().setMaxX(points[0].getX());
        graph.getViewport().setMinX(points[points.length - 1].getX());

    }
}
