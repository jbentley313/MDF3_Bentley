package com.jbentley.bigSimpleBrowser;

import android.os.Bundle;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Menu;

public class WidgetConfigActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_widget_config);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.widget_config, menu);
		return true;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
//		// TODO Auto-generated method stub
//		Bundle extras = getIntent().getExtras();
//		if (extras != null) {
//			int widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
//			
//			if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
//				
//			}
//		}
//		
	}
	
	

}
