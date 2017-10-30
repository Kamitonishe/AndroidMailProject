package ru.mail.android.androidmailproject;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import ru.mail.android.androidmailproject.adapters.MyAdapter;
import ru.mail.android.androidmailproject.dataSingltones.CurrenciesSingletone;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.R.attr.right;

/**
 * Created by dmitrykamaldinov on 10/29/17.
 */

public class GraphsActivity extends AppCompatActivity {

    private GraphView graph;
    private void buildGraph(String baseCurrency, String currency, DataPoint[] points) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);

        series.setTitle(baseCurrency + " in " + currency);
        series.setColor(Color.RED);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(10);

        graph.addSeries(series);

        graph.getViewport().setMinX(points[0].getX());
        graph.getViewport().setMaxX(points[points.length - 1].getX());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);

        graph = (GraphView) findViewById(R.id.graph);


        graph.getGridLabelRenderer().setHumanRounding(false);

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(GraphsActivity.this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space
        graph.getViewport().setXAxisBoundsManual(true);

        Calendar cal = Calendar.getInstance();
        cal.set(2017, 4, 1);
        Date d1 = cal.getTime();
        cal.set(2017, 5, 1);
        Date d2 = cal.getTime();
        cal.set(2017, 6, 1);
        Date d3 = cal.getTime();

        buildGraph("USR", "RUB", new DataPoint[]{
                    new DataPoint(d1, 1),
                    new DataPoint(d2, 5),
                    new DataPoint(d3, 3)
                });

        //series.setDrawBackground(true);
/*
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollableY(true);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0.5);
        graph.getViewport().setMaxX(3.5);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(3.5);
        graph.getViewport().setMaxY(8);
*/

    }
}
