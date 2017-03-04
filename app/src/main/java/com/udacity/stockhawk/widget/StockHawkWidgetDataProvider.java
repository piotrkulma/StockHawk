package com.udacity.stockhawk.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;


import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;
import com.udacity.stockhawk.data.StockUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Piotr Kulma on 04.03.2017.
 */

public class StockHawkWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory  {
    private List list;
    private Context context;

    private final DecimalFormat dollarFormatWithPlus;
    private final DecimalFormat dollarFormat;
    private final DecimalFormat percentageFormat;

    private static final class ShockHawkDataModel {
        private String symbol;
        private String price;
        private String change;
        private String percentage;
        private float rawAbsoluteChange;
        private float percentageChange;

        private ShockHawkDataModel() {
        }

        public static final class ShockHawkDataModelBuilder {
            private ShockHawkDataModelBuilder() {
            }

            public static ShockHawkDataModelBuilder getBuilder() {
                return new ShockHawkDataModelBuilder();
            }

            public ShockHawkDataModel build(Cursor cursor) {
                ShockHawkDataModel model = new ShockHawkDataModel();
                model.symbol = cursor.getString(Contract.Quote.POSITION_SYMBOL);
                model.price = StockUtils.getDollarFormat().format(cursor.getFloat(Contract.Quote.POSITION_PRICE));

                model.rawAbsoluteChange = cursor.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
                model.percentageChange = cursor.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);

                model.change = StockUtils.getDollarFormatWithPlus().format(model.rawAbsoluteChange);
                model.percentage = StockUtils.getPercentageFormat().format(model.percentageChange / 100);

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
    }

    public StockHawkWidgetDataProvider(Context context, Intent intent) {
        this.list = new ArrayList();
        this.context = context;

        dollarFormat = StockUtils.getDollarFormat();
        dollarFormatWithPlus = StockUtils.getDollarFormatWithPlus();
        percentageFormat = StockUtils.getPercentageFormat();
    }

    @Override
    public void onCreate() {
        updateData();
    }

    @Override
    public void onDataSetChanged() {
        updateData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        ShockHawkDataModel model = (ShockHawkDataModel)list.get(position);

        RemoteViews mView = new RemoteViews(context.getPackageName(), R.layout.list_item_quote);

        mView.setTextViewText(R.id.symbol, model.symbol);
        mView.setTextViewText(R.id.price, model.price);

        int activeChangeView;

        if (model.rawAbsoluteChange > 0) {
            activeChangeView = R.id.change;
            mView.setViewVisibility(R.id.change, View.VISIBLE);
            mView.setViewVisibility(R.id.change_red, View.GONE);

        } else {
            activeChangeView = R.id.change_red;
            mView.setViewVisibility(R.id.change, View.GONE);
            mView.setViewVisibility(R.id.change_red, View.VISIBLE);
        }

        if (PrefUtils.getDisplayMode(context).equals(context.getString(R.string.pref_display_mode_absolute_key))) {
            mView.setTextViewText(activeChangeView, model.change);
        } else {
            mView.setTextViewText(activeChangeView, model.percentage);
        }

        return mView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private void updateData() {
        list.clear();

        ShockHawkDataModel.ShockHawkDataModelBuilder builder = ShockHawkDataModel.ShockHawkDataModelBuilder.getBuilder();

        Cursor cursor = context.getContentResolver().query(
                Contract.Quote.URI,
                Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
                null, null, Contract.Quote.COLUMN_SYMBOL);

        while (cursor.moveToNext()) {
            list.add(builder.build(cursor));
        }
    }
}
