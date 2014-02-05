package com.jbentley.week1;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

public class MainActivity extends Activity {
static String Tag = "MainActivity";
WebView myWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
	    	Log.i(Tag, "THERE IS DAZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
	    	Log.i("the effin thing is", data.getHost());
	    	
	    	String urlString = ("http://" + data.getHost());
	    	myWebView.loadUrl(urlString);
	    }
	 
	    
//		Uri uri = Uri.parse("http://www.latimes.com");
//		 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//		 startActivity(intent);
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
