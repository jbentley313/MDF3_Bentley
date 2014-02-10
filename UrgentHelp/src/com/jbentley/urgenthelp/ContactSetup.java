package com.jbentley.urgenthelp;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactSetup extends Activity implements OnClickListener {
	public final static  String Tag = "ContactSetup";
	Button addBtn;
	Button saveBtn;
	Button cancelBtn;
	EditText contactEdittext;
	Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_setup);

		addBtn = (Button) findViewById(R.id.addNewContact);
		saveBtn = (Button) findViewById(R.id.saveBtn);
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		contactEdittext = (EditText) findViewById(R.id.enterContactEdittext);

		addBtn.setOnClickListener(this);
		saveBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		
		//get the preferences
		SharedPreferences prefs = getSharedPreferences("userPrefs",0);
		
		Map<String,?> prefString = prefs.getAll();

		for(Map.Entry<String,?> entry : prefString.entrySet()){
		            Log.d("map values",entry.getKey() + ": " + 
		                                   entry.getValue().toString());            
		 }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_setup, menu);
		return true;
	}

	//button
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.addNewContact:
			contactEdittext.setVisibility(View.VISIBLE);
			saveBtn.setVisibility(View.VISIBLE);
			cancelBtn.setVisibility(View.VISIBLE);
			addBtn.setVisibility(View.GONE);
			break;

		case R.id.cancelBtn:
			contactEdittext.setVisibility(View.GONE);
			contactEdittext.setText("");
			saveBtn.setVisibility(View.GONE);
			cancelBtn.setVisibility(View.GONE);
			addBtn.setVisibility(View.VISIBLE);
			break;

		case R.id.saveBtn:
			

			String emailString = contactEdittext.getText().toString();
			Long timeStamp = new Date().getTime();
			
			
			String currentDateTime = Long.toString(timeStamp);

			//get the preferences
			SharedPreferences prefs = getSharedPreferences("userPrefs",0);
			SharedPreferences.Editor editPrefs = prefs.edit();

			editPrefs.putString(currentDateTime,emailString);
			if (emailString.isEmpty()) {
				Toast.makeText(this, "Please enter an email address", Toast.LENGTH_LONG).show();
			} else {
				addBtn.setVisibility(View.VISIBLE);
				editPrefs.commit();
				contactEdittext.setVisibility(View.GONE);
				contactEdittext.setText("");
				saveBtn.setVisibility(View.GONE);
				cancelBtn.setVisibility(View.GONE);
				addBtn.setVisibility(View.VISIBLE);
			}
			


			break;
		}




	}

}
