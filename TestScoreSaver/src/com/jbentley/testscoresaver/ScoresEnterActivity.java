package com.jbentley.testscoresaver;

import com.jbentley.connectivityPackage.connectivityClass;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class ScoresEnterActivity extends Activity {
	private WebView scoresEnterWebview;
	Context mContext;
	ProgressBar progressBar;

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

		scoresEnterWebview.getSettings().setJavaScriptEnabled(true);

		scoresEnterWebview.setWebViewClient(new myWebViewClient());


		connectivityClass connectionCheck = new connectivityClass();
		if(connectionCheck.connectionStatus(mContext))
		{
			launchURL();
		}


	}

//open Url
	
	private void launchURL() {
		
		scoresEnterWebview.loadUrl(SCORE_WEB_URL);
		
	}
	
	//progress bar
		private class myWebViewClient extends WebViewClient {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap bitMap) {
				Log.i("WVC", "onPageStarted");

				super.onPageStarted(view, url, bitMap);
				findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {

				findViewById(R.id.progressBar).setVisibility(View.GONE);
				
			}
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scores_enter, menu);
		return true;
	}

}
