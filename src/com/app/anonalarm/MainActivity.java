package com.app.anonalarm;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/** TabHost hosts tabs */
		TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);

		/** tid1 is firstTabSpec Id. Its used to access outside. */
		TabSpec firstTabSpec = tabHost.newTabSpec("tid1");
		TabSpec secondTabSpec = tabHost.newTabSpec("tid1");

		firstTabSpec.setIndicator("Alarms").setContent(new Intent(this,AlarmSide.class));
		secondTabSpec.setIndicator("Recording").setContent(new Intent(this,RecordSide.class));

		/** Add tabSpec to the TabHost to display. */
		tabHost.addTab(firstTabSpec);
		tabHost.addTab(secondTabSpec);

	}
}