package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.stockhawk.R;

import timber.log.Timber;

public class StockHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_history);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.STOCK_HISTORY_EXTRA_MESSAGE);

        Timber.d("StockHistoryActivity intent message %s", message);
    }
}
