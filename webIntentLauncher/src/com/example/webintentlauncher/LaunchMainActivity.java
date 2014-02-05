package com.example.webintentlauncher;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LaunchMainActivity extends Activity implements OnClickListener{
EditText urlEditText;
String urlToPass;
Button goButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_main);
        
        urlEditText = (EditText) findViewById(R.id.urledText);
        urlEditText.setText("http://www.cnn.com");
        
        
        urlToPass = urlEditText.toString();
        goButton = (Button) findViewById(R.id.goButton);
        goButton.setOnClickListener(this);
        
        
        
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.launch_main, menu);
        return true;
    }


	@Override
	public void onClick(View v) {
		urlToPass = urlEditText.getText().toString();
		Log.i("LMA", urlToPass.toString());
		// TODO Auto-generated method stub
		Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToPass));
		
		
        startActivity(myIntent);
	}
    
}
