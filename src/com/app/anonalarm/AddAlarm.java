package com.app.anonalarm;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

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
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		int currentTimestamp = (int)System.currentTimeMillis();
		String time = prefs.getString("preferences_start_time","");
		String repeat = prefs.getString("preferences_weekdays","");
		int enable = prefs.getBoolean("preferences_enabled",true) ? 1:0;
		int vibrate = prefs.getBoolean("preferences_vibrate",true) ? 1 :0;
		int volume = prefs.getInt("preferences_volume", 100);
		String filter = prefs.getString("preferences_filter", "");
		int snooze = prefs.getBoolean("preferences_snooze", true) ? 1:0;
		String label = prefs.getString("preferences_label", "Alarm");
		String sound = "aSoundFile.mp3";
		AlarmItem ai = new AlarmItem(currentTimestamp, label,time,repeat,vibrate,volume,
	    		filter,snooze,sound,enable);

        AlarmDatabase db = new AlarmDatabase(this);
        db.addAlarmItem(ai);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("created",1);
        setResult(RESULT_OK,returnIntent);      
		finish();
        
	}
	public void onCancelClick(View v){
		finish();
	}
}