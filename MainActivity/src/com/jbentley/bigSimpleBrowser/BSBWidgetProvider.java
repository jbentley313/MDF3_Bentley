package com.jbentley.bigSimpleBrowser;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;

import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

public class BSBWidgetProvider extends AppWidgetProvider {
	//	public static String DISPLAY_LASTURL = "DisplayLastURL";

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		//		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub


		
		
		String urlVisited = intent.getStringExtra("urlVisited");
		if(urlVisited != null){
			Log.i("ZZZZZZZZZZZZZURL", urlVisited);

			
				Log.i("RECEIVED onReceive", urlVisited);
				super.onReceive(context, intent);

				RemoteViews rv =  new RemoteViews(context.getPackageName(), R.layout.bsbwidget_layout);
				rv.setTextViewText(R.id.latestHeadlines, urlVisited
						.replace("http://", "")
						.replace("https://", ""));

				Intent buttonIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlVisited));
				PendingIntent pi = PendingIntent.getActivity(context, 0, buttonIntent, 0);

				rv.setOnClickPendingIntent(R.id.launchBtn, pi);

				ComponentName widget = new ComponentName(context, BSBWidgetProvider.class);
				AppWidgetManager.getInstance(context).updateAppWidget(widget, rv);



			}
			
		
		
	}



	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		//		Log.i("UPDATE Update", "MSG");
		
	}



}
