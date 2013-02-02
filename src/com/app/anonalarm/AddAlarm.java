package com.app.anonalarm;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;

public class AddAlarm extends PreferenceActivity {
	/** Called when the activity is first created. */
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.addalarm);
		setContentView(R.layout.saveprefs);

	}
	public void onSaveClick(View v){
		//save prefs
		finish();
        
	}
	public void onCancelClick(View v){
		finish();
	}
}