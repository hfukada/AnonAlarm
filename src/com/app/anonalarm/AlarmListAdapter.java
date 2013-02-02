package com.app.anonalarm;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class AlarmListAdapter  extends ArrayAdapter<AlarmItem>{
	
    private ArrayList<AlarmItem> items;

    AlarmDatabase db;
    
	public AlarmListAdapter(Context context, int textViewResourceId, ArrayList<AlarmItem> items) {
        super(context, textViewResourceId, items);
        this.items = items;
        db = new AlarmDatabase(context);
        
}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.alarmitem, null);
        }
        final AlarmItem ai = items.get(position);
        if (ai != null) {
            TextView tt = (TextView) v.findViewById(R.id.alarmlabel);
            CheckBox bt = (CheckBox) v.findViewById(R.id.alarmEnable);
            TextView id = (TextView) v.findViewById(R.id.alarmid);
            if (tt != null) {
            	tt.setText(ai.getLABEL());                            
            }
            if(bt != null){
            	bt.setChecked(ai.getENABLE() == 1? true : false);
            }
            if(id != null){
            	id.setText(ai.getID()+"");
            }
            bt.setOnClickListener(new CheckBox.OnClickListener(){
            	public void onClick(View v){
            		CheckBox cb = (CheckBox) v;
            		cb.setChecked(cb.isChecked());
            		ai.setENABLE(cb.isChecked()?1:0);
            		db.updateAlarmItem(ai);
            	}
            });
        }
        	
        return v;
	}
}
