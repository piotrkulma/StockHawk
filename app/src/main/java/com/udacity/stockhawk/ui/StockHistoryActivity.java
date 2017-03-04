package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.widget.ShockHawkDataModel;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class StockHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_history);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.STOCK_HISTORY_EXTRA_MESSAGE);

        drawChart(message);

        Timber.d("StockHistoryActivity intent message %s", message);
    }

    private void drawChart(String symbol) {
        LineChart lineChart = (LineChart) findViewById(R.id.chart);
        lineChart.setBackgroundColor(Color.GRAY);

        setChartData(lineChart, symbol);

        lineChart.invalidate();
    }

    private void setChartData(LineChart lineChart, String symbol) {
        Cursor cursor = getContentResolver().query(
                Contract.Quote.URI,
                Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
                Contract.Quote.COLUMN_SYMBOL + " = ? ", new String[] {symbol},
                Contract.Quote.COLUMN_SYMBOL);

        ShockHawkDataModel.ShockHawkDataModelBuilder builder =
                ShockHawkDataModel
                        .ShockHawkDataModelBuilder.getBuilder();

        cursor.moveToNext();
        ShockHawkDataModel model = builder.build(cursor, true);

        Timber.d("ShockHawkDataModel %s" , model);

        List entries = new ArrayList();
        String[] quarters = new String[model.getHistory().length];

        if(model != null && model.hasHistory()) {
            for(int i=model.getHistory().length-1; i>=0; i--) {
                int index = (model.getHistory().length-1) - i;

                ShockHawkDataModel.ShockHawkDataHistoryModel history = model.getHistory()[i];

                entries.add(new Entry(index, history.getRawClose(), history.getClose()));

                quarters[index] = history.getDate();
            }
        }

        LineDataSet dataSet = new LineDataSet(entries, model.getSymbol());

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        setAxisFormatters(lineChart, quarters);
    }

    private void setAxisFormatters(LineChart lineChart, final String[] quarters) {

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return quarters[(int) value];
            }
        };

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
    }
}
