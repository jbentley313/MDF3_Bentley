/*
 * WidgetConfigActivity allows the widget to be configured with an initial URL with intent when installed
 * When the user visits any url on Big Simple Browser, the latest URL that is visited shows up in the widget along
 * with a pending intent on the logo as a button
 * 
 * Author: Jason Bentley
 */
package com.jbentley.bigSimpleBrowser;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;

public class WidgetConfigActivity extends Activity implements OnClickListener{
	EditText defaultWebPage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_widget_config);

		defaultWebPage = (EditText) this.findViewById(R.id.defaultWebEditText);
		Button installBtn = (Button) this.findViewById(R.id.instalBtn);
		installBtn.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.widget_config, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.i("CONFIG", "install button");

		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			int WidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);

			//if not an invalid widgetid 
			if (WidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {

				//get edittext text
				String passThisURL = defaultWebPage.getText().toString();

				//remote views
				RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.bsbwidget_layout);

				//check to see if it is a valid url
				if(isValidUrl(passThisURL)){

					//set text
					remoteViews.setTextViewText(R.id.latestHeadlines, passThisURL);
					String strippedURL = passThisURL
							.replace("http://", "")
							.replace("https://", "");
					//attatch button intent to pending intent 
					Intent defaultButtonIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + strippedURL));
					PendingIntent defaultpi = PendingIntent.getActivity(this, 0, defaultButtonIntent, 0);

					//set onclick install button
					remoteViews.setOnClickPendingIntent(R.id.launchBtn, defaultpi);

					ComponentName widget = new ComponentName(getApplicationContext(), BSBWidgetProvider.class);
					AppWidgetManager.getInstance(this).updateAppWidget(widget, remoteViews);

					Intent resultValue = new Intent();
					resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, WidgetId);
					setResult(RESULT_OK, resultValue);

					
					
					finish();

				} else{
					AlertDialog.Builder builder = new AlertDialog.Builder(this)
					.setMessage("This is not a valid URL, please try again.")
					.setCancelable(true);
					builder.create().show();
				}


			}
		}

	}

	private boolean isValidUrl(String url) {
		Pattern p = Patterns.WEB_URL;
		Matcher m = p.matcher(url);
		if(m.matches())
			return true;
		else
			return false;
	}

}
