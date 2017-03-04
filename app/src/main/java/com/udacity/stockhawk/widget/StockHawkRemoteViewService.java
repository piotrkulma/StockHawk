package com.udacity.stockhawk.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Piotr Kulma on 04.03.2017.
 */

public class StockHawkRemoteViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        StockHawkWidgetDataProvider dataProvider = new StockHawkWidgetDataProvider(getApplicationContext(), intent);
        return dataProvider;
    }
}
