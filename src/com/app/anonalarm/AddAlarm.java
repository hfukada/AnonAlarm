package com.app.anonalarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
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
        Calendar cur_cal = new GregorianCalendar();
        cur_cal.setTimeInMillis(System.currentTimeMillis());//set the current time and date for this calendar

        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_YEAR, cur_cal.get(Calendar.DAY_OF_YEAR));
        cal.set(Calendar.HOUR_OF_DAY, cur_cal.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cur_cal.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cur_cal.get(Calendar.SECOND)+5);
        cal.set(Calendar.MILLISECOND, cur_cal.get(Calendar.MILLISECOND));
        cal.set(Calendar.DATE, cur_cal.get(Calendar.DATE));
        cal.set(Calendar.MONTH, cur_cal.get(Calendar.MONTH));

        Intent intent = new Intent(this.getApplicationContext(), AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent, 0);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        //alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent);
        alarm.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
		finish();
        
	}
	public void onCancelClick(View v){
		finish();
	}
}