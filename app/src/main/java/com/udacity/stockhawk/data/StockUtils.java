package com.udacity.stockhawk.data;

import yahoofinance.Stock;

/**
 * Created by Piotr Kulma on 04.03.2017.
 */

public final class StockUtils {
    private StockUtils() {
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
