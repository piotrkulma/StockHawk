package com.udacity.stockhawk.data;

import android.util.Log;

import java.io.IOException;
import java.util.Map;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/**
 * Created by Piotr Kulma on 04.03.2017.
 */

public final class StockUtils {
    public static final String LOGGING_KEY = StockUtils.class.getName();
    private StockUtils() {
    }

    /**
     * We can assume that if stock object fetched from Yahoo services
     * does not contain name (name equals null) whole object is incorrect
     * and stock probably does not exist.
     *
     * @param symbol stock symbol
     * @return whether or not Stock object contains correct data
     */
    public static boolean isCorrectStock(String symbol) {
        Stock stock = null;
        try {
            stock = YahooFinance.get(symbol);
        } catch (IOException ioe) {
            Log.e(LOGGING_KEY, "Connection error, cannot connect to YahooFinance service");
        }

        return isCorrectStock(stock);
    }

    /**
     * We can assume that if stock object fetched from Yahoo services
     * does not contain name (name equals null) whole object is incorrect
     * and stock probably does not exist.
     *
     * @param stock
     * @return whether or not Stock object contains correct data
     */
    public static boolean isCorrectStock(Stock stock) {
        return stock != null && stock.getName() != null;
    }
}
