package com.app.anonalarm;

import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver  extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "Alarm worked.", Toast.LENGTH_LONG).show();
	}/*
	public void playItem(String filename){
		stopRecording();
		if (mPlayer != null && mPlayer.isPlaying())
			mPlayer.release();
			mPlayer =null;
		if (mPlayer == null)
			mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(filename);
			mPlayer.prepare();
			mPlayer.start();
		} catch (IOException e) {
			Log.e("Playing", e.toString());
		}
		
	}*/
}