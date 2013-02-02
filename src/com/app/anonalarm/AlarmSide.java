package com.app.anonalarm;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmSide extends ListActivity {
	/** Called when the activity is first created. */

    ArrayList<AlarmItem> listItems=new ArrayList<AlarmItem>();

    //DEFINING STRING ADAPTER WHICH WILL HANDLE DATA OF LISTVIEW
    AlarmListAdapter adapter;
    ListView alarmlist;
    AlarmDatabase db;
	final static int ALARM_RESULT = 0;
	List<AlarmItem> items;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		db = new AlarmDatabase(this);
		/* First Tab Content */
		setContentView(R.layout.alarmside);
		alarmlist = (ListView) findViewById(android.R.id.list);
		alarmlist.setClickable(true);
		alarmlist.setItemsCanFocus(false);
		alarmlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			   @Override
			   public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg) {
			      //Object listItem = alarmlist.getItemAtPosition(position);
			      Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
			      
			   } 
			});	
		alarmlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				AlarmItem ai = listItems.get(position);
				Toast.makeText(getApplicationContext(), ai.getLABEL() + " was removed.", Toast.LENGTH_SHORT).show();
				db.deleteAlarmItem(ai);
				listItems.remove(position);
				adapter.notifyDataSetChanged();
				return true;
			}
		});
        adapter=new AlarmListAdapter(this,R.id.alarmlabel,listItems);
        setListAdapter(adapter);
        items=db.getAllAlarmItems();
        for (int i = 0; i<items.size(); i++){
            listItems.add(items.get(i));
        }
        adapter.notifyDataSetChanged();

        
        
	}
	public void addAlarmBtn(View v) {
        Intent goToNextActivity = new Intent(getApplicationContext(), AddAlarm.class);
        startActivityForResult(goToNextActivity, ALARM_RESULT);
    }
	public void onCheckboxClicked(View v){
        TextView id = (TextView)v.findViewById(R.id.alarmid);
		Toast toast = Toast.makeText(getApplicationContext(), id.getText(), Toast.LENGTH_SHORT);
		toast.show();
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    switch(requestCode) {
	    case ALARM_RESULT: 
	          if (resultCode == RESULT_OK) {
	              items = db.getAllAlarmItems();
	              listItems.add(items.get(0));
	              
	              adapter.notifyDataSetChanged();
	          }
	    }
    }   

}