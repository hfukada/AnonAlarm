package com.app.anonalarm;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AlarmSide extends ListActivity {
	/** Called when the activity is first created. */

    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING STRING ADAPTER WHICH WILL HANDLE DATA OF LISTVIEW
    ArrayAdapter<String> adapter;
    ListView alarmlist;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* First Tab Content */
		setContentView(R.layout.alarmside);
		alarmlist = (ListView) findViewById(android.R.id.list);
		alarmlist.setClickable(true);
		alarmlist.setItemsCanFocus(false);
		alarmlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			   @Override
			   public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
			      //Object listItem = alarmlist.getItemAtPosition(position);
			      Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
			   } 
			});	
		alarmlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Long", Toast.LENGTH_SHORT).show();
				return false;
			}
		});
        adapter=new ArrayAdapter<String>(this,R.layout.alarmitem,R.id.alarmlabel,listItems);
        setListAdapter(adapter);
	}
	public void addAlarmBtn(View v) {
        Intent goToNextActivity = new Intent(getApplicationContext(), AddAlarm.class);
        startActivityForResult(goToNextActivity, 0);
        //listItems.add("New Alarm");
        //adapter.notifyDataSetChanged();
    }
	public void onCheckboxClicked(View v){
		Toast toast = Toast.makeText(getApplicationContext(), "Ticked", Toast.LENGTH_SHORT);
		toast.show();
	}
}