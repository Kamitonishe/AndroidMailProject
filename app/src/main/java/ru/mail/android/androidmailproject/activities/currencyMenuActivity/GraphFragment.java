package ru.mail.android.androidmailproject.activities.currencyMenuActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import ru.mail.android.androidmailproject.R;
import ru.mail.android.androidmailproject.auxiliary.DateManager;
import ru.mail.android.androidmailproject.data.CurrenciesSingletone;

/**
 * Created by dmitrykamaldinov on 10/31/17.
 */

public class GraphFragment extends Fragment {
    private String baseCurrency, currencyToCompare;
    private TextView latest;
    private GraphView graph;
    private Button chooseDate;

    private String firstDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseCurrency = getArguments().getString("base_currency");
        currencyToCompare = getArguments().getString("currency_to_compare");

        return inflater.inflate(R.layout.graph_fragment_layout, null);
    }

    final DatePickerDialog.OnDateSetListener datePickerListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            //нужно добавить нули
            firstDate = String.valueOf(year) + "-" + String.valueOf(monthOfYear) + "-" + String.valueOf(dayOfMonth);
            updateGraph();
        }
    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        latest = getView().findViewById(R.id.latest_textview);
        graph = getView().findViewById(R.id.graph_);
        chooseDate = getView().findViewById(R.id.choose_date_button);

        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                DatePickerDialog datePicker = new DatePickerDialog(v.getContext(), datePickerListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                cal.set(1990, 11, 1);
                datePicker.setCancelable(true);
                datePicker.getDatePicker().setMinDate(cal.getTime().getTime());
                datePicker.show();
            }
        });

        initGraph();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());

        latest.setTextSize(15);
        if (CurrenciesSingletone.getInstance().hasInfo(baseCurrency, currentDate, currencyToCompare)) {
            Float rate = CurrenciesSingletone.getInstance().getCurrencyRate(baseCurrency, currentDate, currencyToCompare);
            latest.setText("\nКурс на " + currentDate + " : " + rate);
        }
        else {
            latest.setText("Отсутствует интернет-соединение");
            if (CurrenciesSingletone.getInstance().hasInfo(baseCurrency)) {
                String date = CurrenciesSingletone.getInstance().getLatestFeaturedDate(baseCurrency);
                Float rate = CurrenciesSingletone.getInstance().getCurrencyRate(baseCurrency, date, currencyToCompare);
                latest.append("\nКурс на " + date + " : " + rate);
            }
        }
    }

    private void updateGraph() {}

    private void initGraph() {
        graph.getGridLabelRenderer().setHumanRounding(false);

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity(), new SimpleDateFormat("dd.MM.yy")));
        graph.getGridLabelRenderer().setNumHorizontalLabels(4);
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

            if (CurrenciesSingletone.getInstance().hasInfo(baseCurrency, currentDate, currencyToCompare))
                points.add(new DataPoint(cal.getTime().getTime(), baseCurrency.equals(currencyToCompare) ? 1 :
                    CurrenciesSingletone.getInstance().getCurrencyRate(baseCurrency, currentDate, currencyToCompare)));

            if (i == 3)
                firstDate = currentDate;
            currentDate = DateManager.aMonthBefore(currentDate);
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points.toArray(new DataPoint[points.size()]));

        series.setTitle(baseCurrency + " in " + currencyToCompare);
        series.setColor(Color.RED);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(10);
        series.setDrawBackground(true);
        series.setAnimated(true);


        graph.addSeries(series);

        if (points.size() > 0) {
            graph.getViewport().setMinX(points.get(0).getX());
            graph.getViewport().setMaxX(points.get(points.size() - 1).getX());
        }
    }
}
