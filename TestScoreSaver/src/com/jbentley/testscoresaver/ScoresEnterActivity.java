package com.jbentley.testscoresaver;

import java.util.Date;

import com.jbentley.connectivityPackage.connectivityClass;

import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ScoresEnterActivity extends Activity {
	WebView scoresEnterWebview;
	Context mContext;
	ProgressBar progressBar;
	TextView loadingText;

	String fileName = "testScores";

	private static String SCORE_WEB_URL = "http://jbentley313.github.com/MDF3";

	@SuppressLint("SetJavaScriptEnabled") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;

		//Remove titlebar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_scores_enter);



		scoresEnterWebview = (WebView) findViewById(R.id.scoresWebview);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		loadingText = (TextView) findViewById(R.id.loadingText);

		scoresEnterWebview.getSettings().setJavaScriptEnabled(true);

		scoresEnterWebview.setWebViewClient(new myWebViewClient());


		connectivityClass connectionCheck = new connectivityClass();
		if(connectionCheck.connectionStatus(mContext))
		{
			launchURL();
			scoresEnterWebview.getSettings().setAllowFileAccess(true);
			scoresEnterWebview.addJavascriptInterface(new JSInterface(this), "Native");
		}


	}

	public class JSInterface { Context _context;

	JSInterface(Context context) {
		_context = mContext;
	}
	@JavascriptInterface
	public void collectScore(String lastName, String firstName, String score){

		Log.i("From webview JS",lastName + ", " + firstName + ": " + score);
		String scoresToEmail = (lastName + ", " + firstName + ": " + score);

		//use current device time as key
		Long timeStamp = new Date().getTime();
		String currentDateTime = Long.toString(timeStamp);

		//get the preferences
		SharedPreferences prefs = getSharedPreferences("studentScores",0);
		SharedPreferences.Editor editPrefs = prefs.edit();

		editPrefs.putString(currentDateTime,scoresToEmail);

		editPrefs.commit();
	}
	}

	//open Url

	private void launchURL() {

		scoresEnterWebview.loadUrl(SCORE_WEB_URL);

	}

	//progress bar
	private class myWebViewClient extends WebViewClient {

		//security check for proper URL loading the webview
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			final String LOG_TAG = "GitHubCheck";
			Log.d(LOG_TAG, "[x] getHost: " + Uri.parse(url).getHost());
			Log.d(LOG_TAG, "[x] getScheme: " + Uri.parse(url).getScheme());
			Log.d(LOG_TAG, "[x] getPath: " + Uri.parse(url).getPath());
			if (Uri.parse(url).getHost().equals(SCORE_WEB_URL)){return true;}
			return false;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap bitMap) {
			Log.i("WVC", "onPageStarted");

			super.onPageStarted(view, url, bitMap);
			findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
		}

		@Override
		public void onPageFinished(WebView view, String url) {

			progressBar.setVisibility(View.GONE);
			loadingText.setVisibility(View.GONE);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scores_enter, menu);
		return true;
	}

}
