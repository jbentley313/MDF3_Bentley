/*
 *Big Simple Browser
 * 
 * Jason Bentley
 * 
 * 2/6/2014
 * 
 * 
 */

package com.jbentley.bigSimpleBrowser;



import com.jbentley.connectivityPackage.connectivityClass;
import com.jbentley.bigSimpleBrowser.R;

import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;


@SuppressLint("SetJavaScriptEnabled") public class MainActivity extends Activity  implements OnClickListener {
	static String Tag = "MainActivity";
	WebView myWebView;
	Context mContext;
	int fontSize;
	String urlString;
	Button emailBtn;
	Button goBtn;
	ProgressBar progressBar;
	EditText urlEditText;
	
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

		//set webview to zoomable
		myWebView.getSettings().setBuiltInZoomControls(true);

		emailBtn = (Button) findViewById(R.id.emailBtn);
		emailBtn.setOnClickListener(this);
		goBtn = (Button) findViewById(R.id.goBtn);
		goBtn.setOnClickListener(this);
		urlEditText = (EditText) findViewById(R.id.urlEditText);
		urlEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		urlEditText.setOnClickListener(this);


		fontSize = myWebView.getSettings().getDefaultFontSize();

		// Get the intent that started this activity
		Intent callingIntent = getIntent();
		Uri data = callingIntent.getData();

		//set default webpage if launched from icon
		if (data==null) {

			urlString = "http://www.google.com";

		} else {
			//if data, get path 
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

			loadUrl(urlString);

			//allow links to be clickable within the webview
			myWebView.setWebViewClient(new myWebViewClient());

		} else {
			Toast.makeText(mContext, "Please check your network connection", Toast.LENGTH_LONG).show();
		}


		//keyboard 'done' button handler
		urlEditText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
					Log.i(Tag,"Done btn pressed");
					String passedUrlStringFromEdittext = urlEditText.getText().toString()
							.replace("http://", "")
							.replace("https://", "");
					String loadThisUrl = ("http://" + passedUrlStringFromEdittext);
					if(passedUrlStringFromEdittext.isEmpty()) {
						Toast.makeText(mContext, "No URL entered", Toast.LENGTH_LONG).show();
					} else  {
						loadUrl(loadThisUrl);
						Log.i("go button Main Act", passedUrlStringFromEdittext);
					}
				}    
				return false;
			}
		});


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

		// not back button, default system behavior
		return super.onKeyDown(keyCode, event);
	}



	//email button and go button onClick 
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.emailBtn:
			String currentTitle = myWebView.getTitle();
			String currentUrlString = myWebView.getUrl();
			// TODO Auto-generated method stub
			Log.i(Tag, "email button");
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("message/rfc822");
			i.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
			i.putExtra(Intent.EXTRA_SUBJECT, currentTitle);
			i.putExtra(Intent.EXTRA_TEXT, (currentUrlString  + "   --Sent with Big Simple Browser"));
			try {
				startActivity(Intent.createChooser(i, "Email this web page..."));
			} catch (android.content.ActivityNotFoundException ex) {
				Toast.makeText(this, "No email clients are installed.", Toast.LENGTH_LONG).show();
			}

			break;

		case R.id.goBtn:
			String passedUrlStringFromEdittext = urlEditText.getText().toString()
			.replace("http://", "")
			.replace("https://", "");
			String loadThisUrl = ("http://" + passedUrlStringFromEdittext);
			if(passedUrlStringFromEdittext.isEmpty()) {
				Toast.makeText(mContext, "No URL entered", Toast.LENGTH_LONG).show();
			} else  {
				loadUrl(loadThisUrl);
				Log.i("go button Main Act", passedUrlStringFromEdittext);
			}


			break;

		case R.id.urlEditText:
			urlEditText.setText("");
		}


	}


	//progress bar
	private class myWebViewClient extends WebViewClient {

		@Override
		public void onPageStarted(WebView view, String url, Bitmap bitMap) {

			super.onPageStarted(view, url, bitMap);
			findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
		}

		@Override
		public void onPageFinished(WebView view, String url) {

			findViewById(R.id.progressBar).setVisibility(View.GONE);
			String currentUrlString = myWebView.getUrl();
			urlEditText.setText(currentUrlString);

			//widget intent
			Intent widgeIntent = new Intent(getApplicationContext(), BSBWidgetProvider.class);
			widgeIntent.putExtra("urlVisited", currentUrlString);

			//send broadcast to widget
			sendBroadcast(widgeIntent);

			//set webview to zoomable
			myWebView.getSettings().setBuiltInZoomControls(true);
		}
	}

	//load url 
	public void loadUrl(String passedUrlString) {
		Log.i(Tag, "loadURL");
		Log.i("MainAct full string", urlString);
		myWebView.loadUrl(passedUrlString);


		//hide keyboard
		InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(
				Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(urlEditText.getWindowToken(), 0);
	}




}
