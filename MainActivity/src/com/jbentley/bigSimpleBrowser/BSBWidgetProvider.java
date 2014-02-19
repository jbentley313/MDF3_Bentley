package com.jbentley.bigSimpleBrowser;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

public class BSBWidgetProvider extends AppWidgetProvider {
	

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
//		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		
		String urlVisited = intent.getStringExtra("urlVisited");
		Log.i("RECEIVED onReceive", urlVisited);
		
		RemoteViews rv =  new RemoteViews(context.getPackageName(), R.layout.bsbwidget_layout);
		rv.setTextViewText(R.id.latestHeadlines, urlVisited);
		
		ComponentName widget = new ComponentName(context, BSBWidgetProvider.class);
	    AppWidgetManager.getInstance(context).updateAppWidget(widget, rv);
	    super.onReceive(context, intent);
		
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		Log.i("UPDATE Update", "MSG");
		
		
	}

	

}
