package com.udacity.stockhawk.widget;

import android.database.Cursor;

import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.StockUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by Piotr Kulma on 04.03.2017.
 */

public final class ShockHawkDataModel {
    private String symbol;
    private String price;
    private String change;
    private String percentage;
    private float rawAbsoluteChange;
    private float percentageChange;

    private ShockHawkDataHistoryModel[] history = null;

    private ShockHawkDataModel() {
    }

    public static final class ShockHawkDataHistoryModel {
        private String date;
        private long rawDate;
        private String close;
        private float rawClose;

        private ShockHawkDataHistoryModel(BigDecimal close, long time) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");

            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(time);

            this.date = simpleDateFormat.format(date.getTime());
            this.close = StockUtils.getDollarFormat().format(close);
            this.rawClose = close.floatValue();
            this.rawDate = time;
        }

        public static final class ShockHawkDataHistoryModelBuilder {
            private ShockHawkDataHistoryModelBuilder() {
            }

            public static ShockHawkDataHistoryModelBuilder getBuilder() {
                return new ShockHawkDataHistoryModelBuilder();
            }

            public ShockHawkDataHistoryModel[] build(String historyData) {
                ShockHawkDataHistoryModel[] model = null;

                if(historyData != null && historyData.length() > 0) {
                    String lines[] = historyData.split("\n");
                    model = new ShockHawkDataHistoryModel[lines.length];

                    for(int i=0; i<lines.length; i++) {
                        String values[] = lines[i].split(", ");

                        long time = Long.parseLong(values[0]);
                        BigDecimal close = new BigDecimal(values[1]);

                        model[i] = new ShockHawkDataHistoryModel(close, time);
                    }
                }
                return model;
            }
        }


        public String getDate() {
            return date;
        }

        public float getRawClose() {
            return rawClose;
        }

        public String getClose() {
            return close;
        }
    }

    public static final class ShockHawkDataModelBuilder {
        private ShockHawkDataModelBuilder() {
        }

        public static ShockHawkDataModelBuilder getBuilder() {
            return new ShockHawkDataModelBuilder();
        }

        public ShockHawkDataModel build(Cursor cursor) {
            return build(cursor, false);
        }

        public ShockHawkDataModel build(Cursor cursor, boolean appendHistory) {
            ShockHawkDataModel model = new ShockHawkDataModel();
            model.symbol = cursor.getString(Contract.Quote.POSITION_SYMBOL);
            model.price = StockUtils.getDollarFormat().format(cursor.getFloat(Contract.Quote.POSITION_PRICE));

            model.rawAbsoluteChange = cursor.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
            model.percentageChange = cursor.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);

            model.change = StockUtils.getDollarFormatWithPlus().format(model.rawAbsoluteChange);
            model.percentage = StockUtils.getPercentageFormat().format(model.percentageChange / 100);

            if(appendHistory) {
                model.history =
                        ShockHawkDataHistoryModel
                                .ShockHawkDataHistoryModelBuilder
                                .getBuilder().build(cursor.getString(Contract.Quote.POSITION_HISTORY));
            }

            return model;
        }
    }

    public String getSymbol() {
        return symbol;
    }

    public String getPrice() {
        return price;
    }

    public String getChange() {
        return change;
    }

    public String getPercentage() {
        return percentage;
    }

    public float getRawAbsoluteChange() {
        return rawAbsoluteChange;
    }

    public float getPercentageChange() {
        return percentageChange;
    }

    public ShockHawkDataHistoryModel[] getHistory() {
        return history;
    }

    public boolean hasHistory() {
        return history != null && history.length>0;
    }
}

