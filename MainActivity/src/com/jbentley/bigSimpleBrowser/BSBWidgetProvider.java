package com.jbentley.bigSimpleBrowser;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class BSBWidgetProvider extends AppWidgetProvider {

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		
		String urlVisited = intent.getStringExtra("urlVisited");
		Log.i("RECEIVED onReceive", urlVisited);
		
		RemoteViews rv =  new RemoteViews(context.getPackageName(), R.layout.bsbwidget_layout);
		rv.setTextViewText(R.id.latestHeadlines, urlVisited);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		Log.i("RECEIVED Update", "MSG");
	}

	

}
