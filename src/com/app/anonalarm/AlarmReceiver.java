package com.app.anonalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver  extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent();
		i.setClassName("com.app.anonalarm", "com.app.anonalarm.AlarmAlerter");
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Toast.makeText(context,"Passing through rec.",Toast.LENGTH_SHORT).show();
		context.startActivity(i);
	}
}