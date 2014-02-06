package com.jbentley.week1;



import com.jbentley.connectivityPackage.connectivityClass;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener {
	static String Tag = "MainActivity";
	WebView myWebView;
	Context mContext;
	int fontSize;
	String urlString;
	Button emailBtn;
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
		emailBtn = (Button) findViewById(R.id.emailBtn);
		emailBtn.setOnClickListener(this);

		fontSize = myWebView.getSettings().getDefaultFontSize();

		// Get the intent that started this activity
		Intent callingIntent = getIntent();
		Uri data = callingIntent.getData();


		if (data==null) {

			urlString = "http://www.google.com";
		} else {
			String encodedPath = data.getEncodedPath();
			Log.i("MainActivitiy encodedPath", encodedPath);

			//load url into webview if there is data
			Log.i(Tag, "THERE IS Data!");
			Log.i(Tag, data.getHost());

			urlString = ("http://" + data.getHost() + encodedPath);
		}
		connectivityClass connectionChecker = new connectivityClass();

		//check for network connection
		if(connectionChecker.connectionStatus(mContext)) {


			WebSettings myWebSettings = myWebView.getSettings();

			myWebSettings.setJavaScriptEnabled(true);

			Log.i("MainAct full string", urlString);
			myWebView.loadUrl(urlString);

			//allow links to be clickable within the webview
			myWebView.setWebViewClient(new WebViewClient());

		} else {
			Toast.makeText(mContext, "Please check your network connection", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public class WebAppInterface {
		Context mContext;

		//instantiate interface
		WebAppInterface(Context webInterfaceContext) {
			mContext = webInterfaceContext;
		}

		//show javascript popup as a toast
		@JavascriptInterface
		public void showToast(String toast) {
			Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
			Log.i(Tag, "Javascript popup toast");
		}
	}

	//Back button to go back in web history 
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// if back key pressed and there is a history -go back
		if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
			myWebView.goBack();
			return true;
		}

				if (!myWebView.canGoBack()) {
					
					return true;
				}

		// not back button, default system behavior
		return super.onKeyDown(keyCode, event);
	}



	//email button onClick
	@Override
	public void onClick(View v) {
		
		String currentTitle = myWebView.getTitle();

		// TODO Auto-generated method stub
		Log.i(Tag, "email button");
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
		i.putExtra(Intent.EXTRA_SUBJECT, currentTitle);
		i.putExtra(Intent.EXTRA_TEXT, urlString);
		try {
			startActivity(Intent.createChooser(i, "Email this web page..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(this, "No email clients are installed.", Toast.LENGTH_LONG).show();
		}
	}






}
