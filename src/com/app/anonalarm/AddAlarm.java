package com.app.anonalarm;

import java.text.ParseException;
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
		Calendar alarmtime = new GregorianCalendar();
		long currTime=Calendar.getInstance().getTimeInMillis();
		String repeat = prefs.getString("preferences_weekdays","");
		int enable = prefs.getBoolean("preferences_enabled",true) ? 1:0;
		int vibrate = prefs.getBoolean("preferences_vibrate",true) ? 1 :0;
		int volume = prefs.getInt("preferences_volume", 100);
		String filter = prefs.getString("preferences_filter", "");
		int snooze = prefs.getBoolean("preferences_snooze", true) ? 1:0;
		String label = prefs.getString("preferences_label", "Alarm");
		
		/*try {
			alarmtime.setTime(new SimpleDateFormat("EEE-kk:mm").parse(repeat+"-"+prefs.getString("preferences_start_time","")));
		} catch (ParseException err) {
			// TODO Auto-generated catch block
			Log.e("Parse Failure","yeah",err);
		}*/
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

        AlarmDatabase db = new AlarmDatabase(this);
        db.addAlarmItem(ai);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("created",1);
        setResult(RESULT_OK,returnIntent);  
        /*
        Calendar cur_cal = new GregorianCalendar();
        
        Calendar timeOff = new GregorianCalendar();
        //alarmtime.set(Calendar.YEAR, 2013);
        timeOff.set(Calendar.DAY_OF_WEEK, alarmtime.get(Calendar.DAY_OF_WEEK));
        timeOff.set(Calendar.HOUR_OF_DAY, alarmtime.get(Calendar.HOUR_OF_DAY));
        timeOff.set(Calendar.MINUTE, alarmtime.get(Calendar.MINUTE));
        timeOff.set(Calendar.SECOND, 0);
        Calendar curr =  Calendar.getInstance();
        if (timeOff.before(curr)){
        	timeOff.add(Calendar.DAY_OF_WEEK, 7);
        }*/

		Log.d("alarmtimestring",alarmtime.getTime().getDay()+"");
		Log.d("alarmtimestring",alarmtime.getTime().getDate()+"");
		/*
        Log.d("CUrr",(timeOff.getTimeInMillis()-curr.getTimeInMillis())+"");
        Log.d("Alarm",timeOff.getTimeInMillis()+"");*/
        
		ai.setNextTime();
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, ai.getID(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        
        //alarm.set(AlarmManager.RTC_WAKEUP, cur_cal.getTimeInMillis()+1500, pi);
        alarm.set(AlarmManager.RTC_WAKEUP, ai.getTIME(), pi);
		finish();
        
	}
	public void onCancelClick(View v){
		finish();
	}
}