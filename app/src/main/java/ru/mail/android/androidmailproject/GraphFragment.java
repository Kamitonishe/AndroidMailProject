package ru.mail.android.androidmailproject;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ru.mail.android.androidmailproject.JsonModels.Currencies;
import ru.mail.android.androidmailproject.auxiliary.NetworkManager;
import ru.mail.android.androidmailproject.auxiliary.StringManager;
import ru.mail.android.androidmailproject.data.CurrenciesSingletone;

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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());

        latest.setTextSize(20);
        if (CurrenciesSingletone.getInstance().hasInfo(baseCurrency, currentDate)) {
            Currencies cur = CurrenciesSingletone.getInstance().getCurrencyInfo(baseCurrency, currentDate);
            latest.setText("\n\nКурс на " + cur.getDate() + " : " + cur.getRates().get(currencyToCompare));
        }
        else {
            latest.setText("Отсутствует интернет-соединение");
            if (CurrenciesSingletone.getInstance().hasInfo(baseCurrency)) {
                String date = CurrenciesSingletone.getInstance().getLatestFeaturedDate(baseCurrency);
                Currencies cur = CurrenciesSingletone.getInstance().getCurrencyInfo(baseCurrency, date);
                latest.append("\n\nКурс на " + date + " : " + cur.getRates().get(currencyToCompare));
            }
        }
    }

    private void initGraph() {
        graph.getGridLabelRenderer().setHumanRounding(false);

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(4); // only 4 because of the space
        graph.getViewport().setXAxisBoundsManual(true);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());

        Calendar cal = Calendar.getInstance();

        ArrayList<DataPoint> points = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            String[] splited = currentDate.split("-");

            int y = new Integer(splited[0]);
            int m = new Integer(splited[1]);
            int d = new Integer(splited[2]);

            cal.set(y, m - 1, d);

            if (CurrenciesSingletone.getInstance().hasInfo(baseCurrency, currentDate))
                points.add(new DataPoint(cal.getTime(), baseCurrency.equals(currencyToCompare) ? 1 :
                    CurrenciesSingletone.getInstance().getCurrencyInfo(baseCurrency, currentDate).getRates().get(currencyToCompare)));
            currentDate = StringManager.aMonthBefore(currentDate);
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points.toArray(new DataPoint[points.size()]));

        series.setTitle(baseCurrency + " in " + currencyToCompare);
        series.setColor(Color.RED);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(10);
        series.setDrawBackground(true);

        graph.addSeries(series);

        if (points.size() > 0) {
            graph.getViewport().setMinX(points.get(0).getX());
            graph.getViewport().setMaxX(points.get(points.size() - 1).getX());
        }

    }
}
