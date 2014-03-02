package com.jbentley.urgenthelp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Contact_Landing_Activity extends Activity implements OnClickListener {
	
	Button phoneBtn;
	Button emailSetupBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact__landing_);
		
		//instantiate the views
		phoneBtn = (Button) this.findViewById(R.id.phoneBtn);
		emailSetupBtn = (Button) this.findViewById(R.id.emailBtn);
		
		phoneBtn.setOnClickListener(this);
		emailSetupBtn.setOnClickListener(this);
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact__landing_, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		//phone btn
		case R.id.phoneBtn:

			Intent phoneNumberSetupIntent = new Intent(this, PhoneNumberSetup.class);
			startActivity(phoneNumberSetupIntent);
			
			break;

		//email Btn
		case R.id.emailBtn:
			
			Intent emailSetupIntent = new Intent(this, ContactSetup.class);
			startActivity(emailSetupIntent);

			break;

		}
	}

}
