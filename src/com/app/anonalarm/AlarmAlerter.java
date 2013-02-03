package com.app.anonalarm;

import java.io.IOException;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class AlarmAlerter extends Activity{

	AlarmDatabase db;
	MediaPlayer mPlayer;
	AlarmItem ai;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainCode();
	}
	public void mainCode(){
		AlarmDatabase db = new AlarmDatabase(this);
		ai = db.getClosest();
		if (ai.getENABLE()==1){
			Toast.makeText(this, "Alarm worked.", Toast.LENGTH_LONG).show();
			playItem(ai.getSOUND());
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			alertDialog.setTitle("AnonAlarm Alert Time");
			alertDialog.setMessage(ai.getLABEL())
			           .setCancelable(false)
			           .setPositiveButton("Snooze", new DialogInterface.OnClickListener() {
			        	@Override
						public void onClick(DialogInterface dialog, int which) {
								ai.setTIME(ai.getTIME() + 4*1000*60);
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
								ai.setSOUND(soundfile);
								reEnable(1);
								dialog.cancel();
								finish();
							}
			            })
			            .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								if (mPlayer != null){
									mPlayer.stop();
									mPlayer.release();
								}
								reEnable(0);
								dialog.cancel();
								finish();
							}
						});
						
			AlertDialog alertDialogShowing = alertDialog.create();
			alertDialogShowing.show();
		} else {
			Toast.makeText(this, "Alarm Suppressed", Toast.LENGTH_LONG).show();
			Log.i("Surpressed Alarm", "Re-adding");
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
			ai.setSOUND(soundfile);
			reEnable(0);
		}
	}
	public void reEnable(int snooze){
		Intent newIntent = new Intent(this.getBaseContext(), AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this.getBaseContext(), ai.getID(), newIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarm = (AlarmManager)(this.getBaseContext()).getSystemService(Context.ALARM_SERVICE);
        
		if (snooze == 1){
			ai.setTIME(ai.getTIME()+1000*60*3);
		} else {
		    ai.setNextTime();
		}
		alarm.set(AlarmManager.RTC_WAKEUP, ai.getTIME(), pi);
	}
	public void playItem(String filename){
		Handler handler = new Handler();
		Runnable r=new Runnable() 
		{
			public void run() 
			{
				if (mPlayer != null && mPlayer.isPlaying()){
					mPlayer.stop();
					mPlayer.release();
				}
			} 
		};
		mPlayer = new MediaPlayer();
		if (mPlayer != null && mPlayer.isPlaying())
			mPlayer.release();
			mPlayer =null;
		if (mPlayer == null)
			mPlayer = new MediaPlayer();
		try {
			Log.d("FilenameTOPLay:",Environment.getExternalStorageDirectory().getPath()+"/AnonAlarmData/"+filename);
			mPlayer.setDataSource(Environment.getExternalStorageDirectory().getPath()+"/AnonAlarmData/"+filename);
			mPlayer.prepare();
			mPlayer.start();
			mPlayer.setLooping(true);
			handler.postDelayed(r, 60*1000);
			
		} catch (IOException e) {
			Log.e("Playing", e.toString());
		}
	}
}
