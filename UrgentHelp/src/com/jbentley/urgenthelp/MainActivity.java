package com.jbentley.urgenthelp;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity implements OnClickListener{
	public final static  String Tag = "MainActivity";
	Button setupBtn;
	Button helpBtn;
	



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setupBtn = (Button) findViewById(R.id.setup);
		helpBtn = (Button) findViewById(R.id.helpBtn);
		

		setupBtn.setOnClickListener(this);
		helpBtn.setOnClickListener(this);
		


	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	//button onClick
	@Override
	public void onClick(View button) {
		// TODO Auto-generated method stub

		//setup button
		if (button.getId() == R.id.setup) {
			Log.i(Tag, "Setup Button Clicked");

			//contact activity intent
			Intent contactIntent = new Intent(this, ContactSetup.class);
			startActivity(contactIntent);


		}

		//help button
		if(button.getId() == R.id.helpBtn) {
			Log.i(Tag, "Help! Button Clicked");
			new AlertDialog.Builder(this)

			.setTitle("WARNING!")
			.setMessage("SEND URGENT EMAILS FOR HELP?")
			.setPositiveButton("YES", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					//Email contacts

				}
			})
			.setNegativeButton("No", null)
			.show();


		}

	}
}
