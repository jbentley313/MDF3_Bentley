package com.jbentley.urgenthelp;

import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ContactSetup extends Activity implements OnClickListener {
	Button addBtn;
	Button saveBtn;
	Button cancelBtn;
	EditText contactEdittext;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_setup);
		
		addBtn = (Button) findViewById(R.id.addNewContact);
		saveBtn = (Button) findViewById(R.id.saveBtn);
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		contactEdittext = (EditText) findViewById(R.id.enterContactEdittext);
		
		addBtn.setOnClickListener(this);
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
		contactEdittext.setVisibility(View.VISIBLE);
		saveBtn.setVisibility(View.VISIBLE);
		cancelBtn.setVisibility(View.VISIBLE);
		
	}

}
