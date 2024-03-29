/*
 *  Author: Jason Bentley
 *  
 *  Java2 Week3 Project
 *  
 *  Feb 20, 2014
 *  
 *  Urgent Help MainActivity allows users to send an emergency message to saved
 *  emails with location data as well as phone number and address.  This is not intended
 *  to be a replacement for 911.  
 */
package com.jbentley.urgenthelp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, LocationListener{
	public final static  String Tag = "MainActivity";
	Button setupBtn;
	Button helpBtn;
	Button playStopBtn;
	LocationManager locationManager;
	private String provider;
	private Location passedLocation;
	private TextView latText;
	private TextView longText;
	private TextView providerText;
	private TextView addressTextResults;
	MediaPlayer player;
	private String goodPhoneNumber;
	List<Address> addresses;
	Context mContext;
	Geocoder geocoder;
	float lat;
	float lng;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		
		
		boolean agreedtoDisclaimer = getSharedPreferences("FIRSTRUN", MODE_PRIVATE).getBoolean("agreedToDisclaimer", false);
		
		Log.i("AGREED", String.valueOf(agreedtoDisclaimer));
		if (!agreedtoDisclaimer){
		
			new AlertDialog.Builder(this)

			.setTitle("Disclaimer")
			.setMessage("This application is not intended to be a replacement for 911.  If you need emergency help, always dial 911 first.")
			.setPositiveButton("OK, I understand", new DialogInterface.OnClickListener() {

				//set agreed to disclaimer to true 
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					getSharedPreferences("FIRSTRUN", MODE_PRIVATE)
					.edit()
					.putBoolean("agreedToDisclaimer", true)
					.commit();
					
				}

				
			})
			.setNegativeButton("I do not agree", new DialogInterface.OnClickListener() {
				
				//user didn't agree to disclaimer
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					getSharedPreferences("FIRSTRUN", MODE_PRIVATE)
					.edit()
					.putBoolean("agreedToDisclaimer", false)
					.commit();
					finish();
				}
			})
			
			.show()
			.setCancelable(false);
			
			// Save if first run
			getSharedPreferences("FIRSTRUN", MODE_PRIVATE)
			.edit()
			.putBoolean("firstrun", false)
			.commit();
		}

		mContext = this;

		//instantiate views
		setupBtn = (Button) findViewById(R.id.setup);
		helpBtn = (Button) findViewById(R.id.helpBtn);
		playStopBtn = (Button) findViewById(R.id.playStopBtn);
		latText = (TextView) findViewById(R.id.latEditText);
		longText = (TextView) findViewById(R.id.longEditText);
		providerText = (TextView) findViewById(R.id.providerEditText);
		addressTextResults = (TextView) findViewById(R.id.addressResults);

		//set onClick listeners
		setupBtn.setOnClickListener(this);
		helpBtn.setOnClickListener(this);
		playStopBtn.setOnClickListener(this);

		latText.setText("Getting Latitude...");
		longText.setText("Getting Longitude..."); 
		providerText.setText("Getting Provider..."); 

		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		player = MediaPlayer.create(this, R.raw.scanr);
	}

	//button onClick
	@Override
	public void onClick(View button) {
		// TODO Auto-generated method stub

		//setup button
		if (button.getId() == R.id.setup) {
			Log.i(Tag, "Setup Button Clicked");

			//contact activity intent
			Intent contactIntent = new Intent(this, Contact_Landing_Activity.class);
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

					Log.i(Tag, "help button, onClick get lastknown location");

					locationManager =  (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
					boolean GPSenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
					boolean NETWORKenabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
					if (!GPSenabled && !NETWORKenabled) {
						Log.i(Tag, "LOCATION TURNED OFF");
						displayLocationSettingsAlert();
						sendEmailNow();

					} else {

						//last known location
						Location locationtoEmail = passedLocation;

						// Initialize the location fields
						if (locationtoEmail != null) {

							sendEmailNow();

						} else {
							Log.i("MainAct location NULL", "NULL!");
							providerText.setText("Provider not available");
							latText.setText("Latitude not available");
							longText.setText("Longitude not available");
							sendEmailNow();
						}
					}

				}
			})
			.setNegativeButton("No", null)
			.show();
		}




		//play stop audio for police scanner
		if(button.getId() == R.id.playStopBtn) {
			if(player.isPlaying()) {
				player.pause();
				player.seekTo(0);
				playStopBtn.setText(R.string.play_police_scanner);
			} 
			else {
				//start player
				player.start();
				playStopBtn.setText("STOP");
			}
		}

	}


	//location change handler
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

		Log.i(Tag, "Location changed!");

		addressTextResults.setText("");

		lat = (float) (location.getLatitude());
		lng = (float) (location.getLongitude());
		String currentProvider = (location.getProvider().toUpperCase(Locale.getDefault()));

		this.latText.setText(String.valueOf(lat));
		this.longText.setText(String.valueOf(lng));
		this.providerText.setText(currentProvider);
		Log.i(Tag, "Lat= " + lat + ", Long= " + lng + " provider= " + currentProvider);

		//set provider to be used in getting last known location for sending emails
		provider = currentProvider;
		if(provider.equalsIgnoreCase("network")){
			passedLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		} else if (provider.equalsIgnoreCase("gps")){
			passedLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}

		//geocoder to get address based on location
		Geocoder geocoder = new Geocoder(this, Locale.getDefault());
		try {

			this.addresses = geocoder.getFromLocation(lat, lng, 1);

			Address address = addresses.get(0);
			if (address.toString() == "") {
				addressTextResults.setText("Address Unavailable");
			} else {
				int i = 0;
				while (address.getAddressLine(i) != null) {
					addressTextResults.append("\n");
					addressTextResults.append(address.getAddressLine(i++));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(Tag, e.getMessage().toString());
		}
	}

	//called when activity resumes as well as after onCreate
	@Override
	protected void onResume() {

		super.onResume();
		Log.i(Tag, "onResume");


		boolean GPSenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean NETWORKLocEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);

		//if location is turned off, ask user to turn on
		if (!GPSenabled && !NETWORKLocEnabled) {

			this.displayLocationSettingsAlert();

			//gps location request
		} else {
			Log.i(Tag, "GPS LocationUpdates requested");
			Criteria myCriteria = new Criteria();
			myCriteria.setAccuracy(Criteria.ACCURACY_FINE);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000 , 0, this);

		}

		//network location request
		if (!NETWORKLocEnabled) {
			Log.i(Tag, "No Network Location enabled");
		} else {
			Log.i(Tag, "Network LocationUpdates requested");
			//			Criteria myCriteria = new Criteria();
			//			myCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 15000, 0, this);
		}
	}

	//if provider disabled 
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

		Log.i(Tag, "onProviderDisabled " + provider);
	}

	//if provider is enabled 
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

		Log.i(Tag, "onProviderEnabled " + provider);
	}

	//if status changes
	@Override
	public void onStatusChanged(String provider2, int status, Bundle extras) {
		// TODO Auto-generated method stub

		String passProvider = null;

		if(provider2 !=null) {
			passProvider = provider2;
		}

		if(extras !=null) {

			int satellites = extras.getInt("satellites");
			Log.i(Tag, "onStatusChanged " + passProvider + " " + status + " " + satellites);
		}
	}

	//if activity pauses the location listener
	@Override
	protected void onPause() {

		Log.i(Tag, "onPause");
		locationManager.removeUpdates(this);
		
		super.onPause();
	}

	//send email
	void sendEmailNow() {
		String contactString = null;
		String allEmailContacts = null;

		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");

		//get the preferences
		SharedPreferences prefs = getSharedPreferences("userContacts",0);
		Map<String,?> prefString = prefs.getAll();
		ArrayList<String> contactArrayList = new ArrayList<String>();

		//get device telephone number to display in email
		TelephonyManager telephoneMgr = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNum = telephoneMgr.getLine1Number();
		if(phoneNum !=null){
			goodPhoneNumber = phoneNum;
		} else {
			goodPhoneNumber = "unavailable";
		}

		//loop through entries and delete key that is associated with a matched value from argument (emailString)
		for(Map.Entry<String,?> entry : prefString.entrySet()){

			contactString = entry.getValue().toString();
			contactArrayList.add(contactString);
		}

		//replace all "]" and "[" with ""
		allEmailContacts = contactArrayList.toString().replace("[", "").replace("]", "");

		//email intents
		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{allEmailContacts});
		i.putExtra(Intent.EXTRA_SUBJECT, "**Emergency message sent from UrgentHelp**");
		i.putExtra(Intent.EXTRA_TEXT   , "Please send help.  I am at (or near) address: " + 
				addressTextResults.getText().toString() + "\n" + "\n" +
				"My last known Geo coordinates: " + 
				latText.getText().toString() + ", " + 
				longText.getText().toString() + "\n" +  "\n" +
				"If you have received this message, I am in need of immediate help." + "\n" + "\n"+ "My phone number is: " + goodPhoneNumber);
		try {
			startActivity(Intent.createChooser(i, "Send message..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(mContext, "No email clients installed.", Toast.LENGTH_LONG).show();
		}
	}

	//display alert if location is turned off
	private void displayLocationSettingsAlert() {
		new AlertDialog.Builder(this)

		.setTitle("Location is turned off!")
		.setMessage("Would you like to go to location settings to turn location on? (Recommended)")
		.setPositiveButton("YES", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				//send user to settings/location to be able to turn on
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);
			}
		})
		.setNegativeButton("No", null)
		.setCancelable(true)
		.show();
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		
		
		
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
			
		
			
			
		}
		return super.onOptionsItemSelected(item);
	}



	
}
