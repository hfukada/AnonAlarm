package com.app.anonalarm;

import java.io.IOException;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver  extends BroadcastReceiver {

	AlarmDatabase db;
	MediaPlayer mPlayer;
	AlarmItem ai;
	@Override
	public void onReceive(Context context, Intent intent) {
		AlarmDatabase db = new AlarmDatabase(context);
		ai = db.getClosest();
		if (ai.getENABLE()==1){
			Toast.makeText(context, "Alarm worked.", Toast.LENGTH_LONG).show();
			playItem(ai.getSOUND());
			
			/*AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
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
								dialog.cancel();
							}
						});
						
			AlertDialog alertDialogShowing = alertDialog.create();
			alertDialogShowing.show();*/
		} else {
			Toast.makeText(context, "Alarm Suppressed", Toast.LENGTH_LONG).show();
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
			Intent newIntent = new Intent(context, AlarmReceiver.class);
			
	        PendingIntent pi = PendingIntent.getBroadcast(context, ai.getID(), newIntent, PendingIntent.FLAG_CANCEL_CURRENT);
	        AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	        //alarm.set(AlarmManager.RTC_WAKEUP, cur_cal.getTimeInMillis()+1500, pi);
	        ai.setNextTime();
	        alarm.set(AlarmManager.RTC_WAKEUP, ai.getTIME(), pi);
		}
	}
	
	public void playItem(String filename){
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
			//mPlayer.setLooping(true);
		} catch (IOException e) {
			Log.e("Playing", e.toString());
		}

	}
}