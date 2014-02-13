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
import android.os.AsyncTask;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener, LocationListener{
	public final static  String Tag = "MainActivity";
	Button setupBtn;
	Button helpBtn;
	LocationManager locationManager;
	private String provider;
	private TextView latText;
	private TextView longText;
	private TextView providerText;
	private TextView addressTextResults;
	List<Address> addresses;
	Context mContext;
	Geocoder geocoder;
	float lat;
	float lng;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContext = this;

		setupBtn = (Button) findViewById(R.id.setup);
		helpBtn = (Button) findViewById(R.id.helpBtn);
		latText = (TextView) findViewById(R.id.latEditText);
		longText = (TextView) findViewById(R.id.longEditText);
		providerText = (TextView) findViewById(R.id.providerEditText);
		addressTextResults = (TextView) findViewById(R.id.addressResults);

		setupBtn.setOnClickListener(this);
		helpBtn.setOnClickListener(this);

		latText.setText("Getting Latitude...");
		longText.setText("Getting Longitude..."); 
		providerText.setText("Getting Provider..."); 


		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
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

					locationManager =  (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
					boolean GPSenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
					boolean NETWORKenabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
					if (!GPSenabled && !NETWORKenabled) {
						Log.i(Tag, "LOCATION TURNED OFF");
						displayLocationSettingsAlert();


					} else {
						Location location = locationManager.getLastKnownLocation(provider);



						Log.i(Tag, "help button, onClick get lastknown location");

						// Initialize the location fields
						if (location != null) {
							providerText.setText(provider);
							onLocationChanged(location);
							sendEmailNow();
						} else {
							Log.i("MainAct location NULL", "NULL!");
							providerText.setText("Provider not available");
							latText.setText("Location not available");
							longText.setText("Location not available");
							sendEmailNow();
						}
					}

				}
			})
			.setNegativeButton("No", null)
			.show();
		}



	}

	

	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Log.i(Tag, "Location changed!");
		addressTextResults.setText("");

		lat = (float) (location.getLatitude());
		lng = (float) (location.getLongitude());
		String currentProvider = (location.getProvider());

		this.latText.setText(String.valueOf(lat));
		this.longText.setText(String.valueOf(lng));
		this.providerText.setText(currentProvider);
		Log.i(Tag, "Lat= " + lat + ", Long= " + lng + " provider= " + currentProvider);

		//set provider to be used in getting last known location for sending emails
		provider = currentProvider;



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



	@Override
	protected void onResume() {

		super.onResume();
		Log.i(Tag, "onResume");

		
		boolean GPSenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean NETWORKLocEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);

		if (!GPSenabled && !NETWORKLocEnabled) {

			this.displayLocationSettingsAlert();

		} else {
			Log.i(Tag, "GPS LocationUpdates requested");
			Criteria myCriteria = new Criteria();
			myCriteria.setAccuracy(Criteria.ACCURACY_FINE);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 0, this);

		}

		if (!NETWORKLocEnabled) {
			Log.i(Tag, "No Network Location enabled");
		} else {
			Log.i(Tag, "Network LocationUpdates requested");
			//			Criteria myCriteria = new Criteria();
			//			myCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 15000, 0, this);

		}



	}


	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Log.i(Tag, "onProviderDisabled " + provider);
		

		
	}


	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Log.i(Tag, "onProviderEnabled " + provider);
		
	}


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

	@Override
	protected void onPause() {
		
		Log.i(Tag, "onPause");
		locationManager.removeUpdates(this);
		super.onPause();
	}

	void sendEmailNow() {
		String contactString = null;
		String allEmailContacts = null;

		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		//get the preferences

		SharedPreferences prefs = getSharedPreferences("userPrefs",0);
		Map<String,?> prefString = prefs.getAll();
		ArrayList<String> contactArrayList = new ArrayList<String>();
		
		TelephonyManager telephoneMgr = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNum = telephoneMgr.getLine1Number();

		//loop through entries and delete key that is associated with a matched value from argument (emailString)
		for(Map.Entry<String,?> entry : prefString.entrySet()){

			contactString = entry.getValue().toString();
			contactArrayList.add(contactString);
		}




		allEmailContacts = contactArrayList.toString().replace("[", "").replace("]", "");

		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{allEmailContacts});
		i.putExtra(Intent.EXTRA_SUBJECT, "**Emergency message sent from UrgentHelp**");
		i.putExtra(Intent.EXTRA_TEXT   , "Please send help.  I am at (or near) address: " + 
				addressTextResults.getText().toString() + "\n" + "\n" +
				"My last known Geo coordinates: " + 
				latText.getText().toString() + ", " + 
				longText.getText().toString() + "\n" +  "\n" +
				"If you have received this message, I am in need of immediate help." + "\n" + "\n"+ "My phone number is: " + phoneNum);
		try {
			startActivity(Intent.createChooser(i, "Send message..."));
		} catch (android.content.ActivityNotFoundException ex) {
			//			Toast.makeText(getActivity(), "No email clients installed.", Toast.LENGTH_LONG).show();
		}
	}


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



}
