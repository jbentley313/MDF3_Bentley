package com.jbentley.urgenthelp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import android.location.Location;
import android.location.LocationManager;
import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactSetup extends Activity implements OnClickListener, OnItemClickListener {
	public final static  String Tag = "ContactSetup";
	Button addBtn;
	Button saveBtn;
	Button cancelBtn;
	EditText contactEdittext;
	TextView clickToDeleteTextV;
	Context mContext;
	ListView cList;
	ArrayList<String> list = new ArrayList<String>();
	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_setup);

		addBtn = (Button) findViewById(R.id.addNewContact);
		saveBtn = (Button) findViewById(R.id.saveBtn);
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		contactEdittext = (EditText) findViewById(R.id.enterContactEdittext);
		cList = (ListView) findViewById(R.id.cList);
		clickToDeleteTextV = (TextView) findViewById(R.id.clickDelete);

		addBtn.setOnClickListener(this);
		saveBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, list);
		cList.setAdapter(adapter);
		cList.setOnItemClickListener(this);
		
		
			
		

		//get preferences
		SharedPreferences prefs = getSharedPreferences("userPrefs",0);

		Map<String,?> prefString = prefs.getAll();

		for(Map.Entry<String,?> entry : prefString.entrySet()){
			Log.d("email contacts",entry.getKey() + ": " + 
					entry.getValue().toString());   
			String email =  entry.getValue().toString();
			String key = entry.getKey().toString();

			adapter.add(email);

		}
		
		if(adapter.isEmpty()) {
			clickToDeleteTextV.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_setup, menu);
		return true;
	}

	//button clicks
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		//add button
		case R.id.addNewContact:

			//show keyboard for contactedittext
			InputMethodManager inputMethodMgr = (InputMethodManager)
			getSystemService(Context.INPUT_METHOD_SERVICE);
			if(inputMethodMgr != null){
				inputMethodMgr.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
			}
			
			contactEdittext.setVisibility(View.VISIBLE);
			contactEdittext.requestFocus();
			saveBtn.setVisibility(View.VISIBLE);
			cancelBtn.setVisibility(View.VISIBLE);
			addBtn.setVisibility(View.GONE);
			break;

			//cancel button
		case R.id.cancelBtn:
			
			//hide keyboard
			InputMethodManager inputMethodMgr2 = (InputMethodManager)
			getSystemService(Context.INPUT_METHOD_SERVICE);
			if(inputMethodMgr2 != null){
				inputMethodMgr2.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
			
			contactEdittext.setVisibility(View.GONE);
			contactEdittext.setText("");
			saveBtn.setVisibility(View.GONE);
			cancelBtn.setVisibility(View.GONE);
			addBtn.setVisibility(View.VISIBLE);
			break;

			//save button
		case R.id.saveBtn:

			String emailString = contactEdittext.getText().toString();

			if (emailString.isEmpty()) {
				Toast.makeText(this, "Please enter an email address", Toast.LENGTH_LONG).show();
			}
			
			if(isEmailValid(emailString)){
				//use current device time as key
				Long timeStamp = new Date().getTime();
				String currentDateTime = Long.toString(timeStamp);

				//get the preferences
				SharedPreferences prefs = getSharedPreferences("userPrefs",0);
				SharedPreferences.Editor editPrefs = prefs.edit();

				editPrefs.putString(currentDateTime,emailString);
				addBtn.setVisibility(View.VISIBLE);
				editPrefs.commit();
				contactEdittext.setVisibility(View.GONE);
				contactEdittext.setText("");
				saveBtn.setVisibility(View.GONE);
				cancelBtn.setVisibility(View.GONE);
				addBtn.setVisibility(View.VISIBLE);

				Intent refreshConacts = new Intent(this, ContactSetup.class);

				startActivity(refreshConacts);
				
			} else {
				Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_LONG).show();
			}
			
			break;
		}




	}


	//listview click 
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		// TODO Auto-generated method stub

		String emailString = cList.getItemAtPosition(position).toString();

		deleteEmailEntry(emailString);
		Log.i(Tag, emailString);
	}


	//delete email from shared prefs
	public void deleteEmailEntry(final String emailString) {

		//confirm delete
		new AlertDialog.Builder(this)

		.setTitle("Delete Emergency Contact")
		.setMessage("Are you sure you want to delete this contact?")
		.setPositiveButton("YES", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				//get the preferences
				Log.i(Tag, "deleteEm");
				SharedPreferences prefs = getSharedPreferences("userPrefs",0);
				SharedPreferences.Editor editPrefs = prefs.edit();
				Map<String,?> prefString = prefs.getAll();

				//loop through entries and delete key that is associated with a matched value from argument (emailString)
				for(Map.Entry<String,?> entry : prefString.entrySet()){

					String email = entry.getValue().toString();
					if (email.contentEquals(emailString)) {
						Log.i(Tag, "Match");
						editPrefs.remove(entry.getKey());
						editPrefs.commit();
					}

				}

			}
		})
		.setNegativeButton("No", null)
		.show();

	}
	
	//email validate
	public static boolean isEmailValid(CharSequence email) {
		   return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
		};


}