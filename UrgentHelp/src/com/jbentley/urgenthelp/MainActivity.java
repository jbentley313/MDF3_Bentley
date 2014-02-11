package com.jbentley.urgenthelp;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
	Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setupBtn = (Button) findViewById(R.id.setup);
		helpBtn = (Button) findViewById(R.id.helpBtn);
		latText = (TextView) findViewById(R.id.latEditText);
		longText = (TextView) findViewById(R.id.longEditText);
		providerText = (TextView) findViewById(R.id.providerEditText);

		setupBtn.setOnClickListener(this);
		helpBtn.setOnClickListener(this);

		latText.setText("Finding Latitude...");
		longText.setText("Finding Longitude..."); 
		providerText.setText("Getting Provider..."); 
				
		
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		if (!enabled) {

			this.displayLocationASettingsAlert();

		} else {
			locationManager.requestLocationUpdates(provider, 1000, 1, this);
		}
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
					locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
					boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
					if (!enabled) {

						displayLocationASettingsAlert();


					} else {
						//Email contacts
						Location location = locationManager.getLastKnownLocation(provider);


						// Initialize the location fields
						if (location != null) {
							providerText.setText(provider);
							onLocationChanged(location);
						} else {
							latText.setText("Location not available");
							longText.setText("Location not available");
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
		Log.i(Tag, "Location changed!!!!!!!!");
		float lat = (float) (location.getLatitude());
		float lng = (float) (location.getLongitude());

		latText.setText(String.valueOf(lat));
		longText.setText(String.valueOf(lng));
		providerText.setText(provider);

	}


	private void displayLocationASettingsAlert() {
		new AlertDialog.Builder(this)

		.setTitle("Location is turned off.")
		.setMessage("Would you like to go to settings to turn location on? (Recommended)")
		.setPositiveButton("YES", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				//send user to settings/location to be able to turn on
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);
			}
		})
		.setNegativeButton("No", null)
		.show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(provider, 1000, 1, this);
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
		String statusInt = String.valueOf(status);
		
		String satellites = extras.get("satellites").toString();
		Log.i(Tag, "onStatusChanged " + provider2 + " " + statusInt + " " + satellites);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(Tag, "onPause");
		locationManager.removeUpdates(this);
	}
}
