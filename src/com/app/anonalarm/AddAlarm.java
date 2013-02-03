package com.app.anonalarm;

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
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.addalarm);
		setContentView(R.layout.saveprefs);
		prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
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
		
		AlarmItem ai = new AlarmItem(currentTimestamp, label,0,repeat,vibrate,volume,
	    		filter,snooze,soundfile,enable);
		ai.setTIME(alarmtime.getTimeInMillis());


        Intent returnIntent = new Intent();
        returnIntent.putExtra("created",1);
        setResult(RESULT_OK,returnIntent);  
        
        /*
        Calendar timeOff = new GregorianCalendar();
        //alarmtime.set(Calendar.YEAR, 2013);
        timeOff.set(Calendar.DAY_OF_WEEK, alarmtime.get(Calendar.DAY_OF_WEEK));
        timeOff.set(Calendar.HOUR_OF_DAY, alarmtime.get(Calendar.HOUR_OF_DAY));
        timeOff.set(Calendar.MINUTE, alarmtime.get(Calendar.MINUTE));
        timeOff.set(Calendar.SECOND, 0);
        Calendar curr =  Calendar.getInstance();
        if (timeOff.before(curr)){
        	timeOff.add(Calendar.DAY_OF_WEEK, 7);
        }
		
		
        Log.d("Curr",(timeOff.getTimeInMillis()-curr.getTimeInMillis())+"");
        Log.d("Alarm",timeOff.getTimeInMillis()+"");*/
        
        //ai.setTIME(timeOff.getTimeInMillis());
		ai.setNextTime();
		

        AlarmDatabase db = new AlarmDatabase(this);
        db.addAlarmItem(ai);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, ai.getID(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        
        //alarm.set(AlarmManager.RTC_WAKEUP, cur_cal.getTimeInMillis()+1500, pi);
        alarm.set(AlarmManager.RTC_WAKEUP, ai.getTIME(), pi);
		finish();
        
	}
	public void onCancelClick(View v){
		finish();
	}
}