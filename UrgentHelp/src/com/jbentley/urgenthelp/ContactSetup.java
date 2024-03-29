/*
 * Author: Jason Bentley
 *  
 *  Java2 Week3 Project
 *  
 *  Feb 20, 2014
 *  
 *  ContactSetup.java allows users to add or delete email contacts for use with the app.  Data storage is done with
 *  SharedPreferences.
 */
package com.jbentley.urgenthelp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
	MenuItem addIcon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_setup);

		//action bar icon home button
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		
		//intantiate views
		saveBtn = (Button) findViewById(R.id.saveBtn);
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		contactEdittext = (EditText) findViewById(R.id.enterContactEdittext);
		cList = (ListView) findViewById(R.id.cList);
		clickToDeleteTextV = (TextView) findViewById(R.id.clickDelete);

		
		saveBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, list);
		cList.setAdapter(adapter);
		cList.setOnItemClickListener(this);


		//get preferences
		SharedPreferences prefs = getSharedPreferences("userContacts",0);

		Map<String,?> prefString = prefs.getAll();

		for(Map.Entry<String,?> entry : prefString.entrySet()){
			Log.d("email contacts",entry.getKey() + ": " + 
					entry.getValue().toString());   
			String email =  entry.getValue().toString();

			//add email to listview adapter
			adapter.add(email);

		}

		//hide delete instructions if no emails are present in the adapter
		if(adapter.isEmpty()) {
			clickToDeleteTextV.setVisibility(View.GONE);
		}
	}


	//button clicks
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

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
			addIcon.setVisible(true);
			break;

			//save button
		case R.id.saveBtn:

			String emailString = contactEdittext.getText().toString();

			if (emailString.isEmpty()) {
				Toast.makeText(this, "Please enter an email address", Toast.LENGTH_LONG).show();
			}

			//check for valid email then save
			if(isEmailValid(emailString)){
				//use current device time as key
				Long timeStamp = new Date().getTime();
				String currentDateTime = Long.toString(timeStamp);

				//get the preferences
				SharedPreferences prefs = getSharedPreferences("userContacts",0);
				SharedPreferences.Editor editPrefs = prefs.edit();

				editPrefs.putString(currentDateTime,emailString);
				addIcon.setVisible(true);
				editPrefs.commit();
				contactEdittext.setVisibility(View.GONE);
				contactEdittext.setText("");
				saveBtn.setVisibility(View.GONE);
				cancelBtn.setVisibility(View.GONE);
				addIcon.setVisible(true);

				//finish and restart activity
				finish();
				startActivity(getIntent());

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

		//pass email string to delete email method
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
				SharedPreferences prefs = getSharedPreferences("userContacts",0);
				SharedPreferences.Editor editPrefs = prefs.edit();
				Map<String,?> prefString = prefs.getAll();

				//loop through entries and delete key that is associated with a matched value from argument (emailString)
				for(Map.Entry<String,?> entry : prefString.entrySet()){

					String email = entry.getValue().toString();
					if (email.contentEquals(emailString)) {
						Log.i(Tag, "Match");
						editPrefs.remove(entry.getKey());
						editPrefs.commit();

						//finish and restart activity
						finish();
						startActivity(getIntent());
					}
				}
			}
		})
		.setNegativeButton("No", null)
		.show();
	}

	//email validation
	public static boolean isEmailValid(CharSequence email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.contact_setup, menu);
		
		 addIcon = menu.findItem(R.id.action_add);
		
		return super.onCreateOptionsMenu(menu);
	}

	//action bar items click
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;

		case R.id.action_author:
			new AlertDialog.Builder(this)

			.setTitle("About the Author")
			.setMessage("Jason Bentley is currently enrolled at Full Sail University for Mobile Development.  He plans on making coding his new career after graduation.")
			.setPositiveButton("Ok", null)
			
			.setCancelable(true)
			.show();
		
			break;
			
		case R.id.action_add:
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
			item.setVisible(false);
			
			
		}
		return super.onOptionsItemSelected(item);
	}





}
