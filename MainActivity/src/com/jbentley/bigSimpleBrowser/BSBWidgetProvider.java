/*
 * BSBWidgetProvider receives broadcast from MainActivity in Big Simple Browser.  When a url is visited in Big Simple Browser
 * onReceive handles a broadcast message with the last URL visited.  
 * 
 * Author: Jason Bentley
 */
package com.jbentley.bigSimpleBrowser;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

public class BSBWidgetProvider extends AppWidgetProvider {
	public static String Tag = "BSBWidgetProvider";
	String urlVisited;
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		//		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		urlVisited = intent.getStringExtra("urlVisited");
		if(urlVisited != null){
			
			Log.i((Tag + " onReceive"), urlVisited);
			super.onReceive(context, intent);

			//remote widget views
			RemoteViews rv =  new RemoteViews(context.getPackageName(), R.layout.bsbwidget_layout);
			rv.setTextViewText(R.id.latestHeadlines, urlVisited
					.replace("http://", "")
					.replace("https://", ""));

			//buttonIntent and pending intent
			Intent buttonIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlVisited));
			PendingIntent pi = PendingIntent.getActivity(context, 0, buttonIntent, 0);

			//set pending intent to launch btn
			rv.setOnClickPendingIntent(R.id.launchBtn, pi);

			ComponentName widget = new ComponentName(context, BSBWidgetProvider.class);
			AppWidgetManager.getInstance(context).updateAppWidget(widget, rv);

		}
	}

	//update handler
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub


	}



}
