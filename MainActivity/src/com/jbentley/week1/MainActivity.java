package com.jbentley.week1;



import com.jbentley.connectivityPackage.connectivityClass;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class MainActivity extends Activity {
	static String Tag = "MainActivity";
	WebView myWebView;
	Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mContext = this;
		
		super.onCreate(savedInstanceState);
		Log.i(Tag, "on create");
		//hide titlebar and make fullscreen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);

		myWebView = (WebView) findViewById(R.id.webView);


		// Get the intent that started this activity
		Intent callingIntent = getIntent();
		Uri data = callingIntent.getData();

		if (data==null) {
			Log.i(Tag, "NO DATA");
		} else {

			//load url into webview if there is data
			Log.i(Tag, "THERE IS Data!");
			Log.i(Tag, data.getHost());

			String urlString = ("http://" + data.getHost());

			connectivityClass connectionChecker = new connectivityClass();


			//check for network connection
			if(connectionChecker.connectionStatus(mContext)) {
				myWebView.loadUrl(urlString);
				
				myWebView.getSettings().setJavaScriptEnabled(true);
				
				//allow links to be clickable within the webview
				myWebView.setWebViewClient(new WebViewClient());
			} else {
				Toast.makeText(mContext, "Please check your network connection", Toast.LENGTH_LONG).show();
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
