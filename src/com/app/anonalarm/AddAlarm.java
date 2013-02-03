package com.app.anonalarm;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class AddAlarm extends PreferenceActivity {
	/** Called when the activity is first created. */
	SharedPreferences prefs;
	AlarmDatabase db;
	AlarmItem ai;
	Intent returnIntent;
	Bundle alarmidBundle =null;
	int alarmid;
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.addalarm);
		setContentView(R.layout.saveprefs);
		prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		Log.d("made it","here1");
		SharedPreferences.Editor prefEditor = prefs.edit();
		Preference listPref = findPreference("preferences_weekdays");
		ListPreferenceMultiSelect lpms = (ListPreferenceMultiSelect) listPref;
		lpms.setSummary("Sun,Mon,Tue,Wed,Thu,Fri,Sat");
		prefEditor.putString("preferences_weekdays","Sun,Mon,Tue,Wed,Thu,Fri,Sat");
		Preference listPref1 = findPreference("preferences_filter");
		ListPreferenceMultiSelect lpms1 = (ListPreferenceMultiSelect) listPref1;
		lpms1.setSummary("None");
		prefEditor.putString("preferences_filter","");
		try{
			alarmidBundle = this.getIntent().getExtras();
			alarmid = alarmidBundle.getInt("alarmId");
			ai = db.getAlarmItem(alarmid);
			prefEditor.putString("preferences_weekdays",ai.getREPEAT());
			Timestamp ts = new Timestamp(ai.getTIME());
        	SimpleDateFormat format = new SimpleDateFormat("kk:mm");

			prefEditor.putString("preferences_start_time",format.format(ts));
			prefEditor.putString("preferences_weekdays",ai.getREPEAT());
			prefEditor.putString("preferences_filter",ai.getFILTER());
			prefEditor.putString("preferences_label",ai.getLABEL());

			prefEditor.putBoolean("preferences_enabled",ai.getENABLE()==1);
			prefEditor.putBoolean("preferences_vibrate",ai.getVIBRATE()==1);
			prefEditor.putBoolean("preferences_snooze",ai.getSNOOZE()==1);
			
			prefEditor.putInt("preferences_volume", 100);
		} catch (Exception e){
			ai = null;
		}
	}
	public void onSaveClick(View v){
		//save prefs
		int currentTimestamp = (int)System.currentTimeMillis();
		Calendar alarmtime = new GregorianCalendar();
		long currTime=Calendar.getInstance().getTimeInMillis();

		String repeat = prefs.getString("preferences_weekdays","");
		int enable = prefs.getBoolean("preferences_enabled",true) ? 1:0;
		int vibrate = prefs.getBoolean("preferences_vibrate",true) ? 1 :0;
		int volume = prefs.getInt("preferences_volume", 100);
		String filter = prefs.getString("preferences_filter", "");
		int snooze = prefs.getBoolean("preferences_snooze", true) ? 1:0;
		String label = prefs.getString("preferences_label", "Alarm");
		
		Log.d("Values",prefs.getString("preferences_start_time", "08:00"));
		String[] timearr = prefs.getString("preferences_start_time", "08:00").split(":");
		alarmtime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timearr[0]));
		alarmtime.set(Calendar.MINUTE, Integer.parseInt(timearr[1]));
		//alarmtime.setTime(new SimpleDateFormat("kk:mm").parse(prefs.getString("preferences_start_time","08:00")));
		DownloadSound sound = new DownloadSound();
		sound.execute("");
		String soundfile = null;
		synchronized(sound) {
			try {
				sound.wait();
			}
			catch(InterruptedException iEx) {}
			soundfile = sound.getFilename();
		}
		Log.d("sql", soundfile);
        db = new AlarmDatabase(this);
        ai=null;
        if( alarmidBundle == null){

    		Log.d("made it","htop");
        	ai = new AlarmItem(currentTimestamp, label,0,repeat,vibrate,volume,
    	    		filter,snooze,soundfile,enable);
    		ai.setTIME(alarmtime.getTimeInMillis());
    		ai.setNextTime();
    		alarmid = ai.getID();
    		returnIntent = new Intent();
    		returnIntent.putExtra("created",1);
        	setResult(RESULT_OK,returnIntent);
        	db.addAlarmItem(ai);
        	
        } else {
        	ai = db.getAlarmItem(alarmid);
        	ai.setLABEL(label);
        	ai.setREPEAT(repeat);
        	ai.setVIBRATE(vibrate);
        	ai.setVOLUME(volume);
        	ai.setFILTER(filter);
        	ai.setSNOOZE(snooze);
        	ai.setENABLE(enable);
    		ai.setTIME(alarmtime.getTimeInMillis());
    		ai.setNextTime();
        	db.updateAlarmItem(ai);
        }
        
    	
	
        
		//Toast.makeText(this,,Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, AlarmReceiver.class);
		Log.d("made it","htop");
        PendingIntent pi = PendingIntent.getBroadcast(this, alarmid, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		Log.d("made it","htop");
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		Log.d("made it","htop");
        
        //alarm.set(AlarmManager.RTC_WAKEUP, cur_cal.getTimeInMillis()+1500, pi);
        
        alarm.set(AlarmManager.RTC_WAKEUP, ai.getTIME(), pi);
		Log.d("made it","htop");
		finish();
        
	}
	public void onCancelClick(View v){
		finish();
	}
}