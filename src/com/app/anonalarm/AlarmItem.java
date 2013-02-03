package com.app.anonalarm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.util.Log;
import android.widget.Toast;

public class AlarmItem{
    private int ID;
    private String LABEL;
    private long TIME;
    private String REPEAT;
    private int VIBRATE;
    private int VOLUME;
    private String FILTER;
    private int SNOOZE;
    private String SOUND;
    private int ENABLE;
    private static final String[] numToDay = {"", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    
    public AlarmItem(int id, String label, long time, String repeat, int vibrate, int volume,
    		String filter, int snooze, String sound, int enable){
    	this.ID = id;
    	this.LABEL = label;
    	this.TIME = time;
    	this.REPEAT = repeat;
    	this.VIBRATE = vibrate;
    	this.VOLUME = volume;
    	this.FILTER = filter;
    	this.SNOOZE = snooze;
    	this.SOUND = sound;
    	this.ENABLE = enable;
    }
    public int getID() {
    	return ID;
    }
	public String getLABEL() {
		return LABEL;
	}

	public void setLABEL(String lABEL) {
		LABEL = lABEL;
	}

	public long getTIME() {
		return TIME;
	}

	public void setTIME(long tIME) {
		TIME = tIME;
	}

	public String getREPEAT() {
		return REPEAT;
	}

	public void setREPEAT(String rEPEAT) {
		REPEAT = rEPEAT;
	}

	public int getVIBRATE() {
		return VIBRATE;
	}

	public void setVIBRATE(int vIBRATE) {
		VIBRATE = vIBRATE;
	}

	public int getVOLUME() {
		return VOLUME;
	}

	public void setVOLUME(int vOLUME) {
		VOLUME = vOLUME;
	}

	public String getFILTER() {
		return FILTER;
	}

	public void setFILTER(String fILTER) {
		FILTER = fILTER;
	}

	public int getSNOOZE() {
		return SNOOZE;
	}

	public void setSNOOZE(int sNOOZE) {
		SNOOZE = sNOOZE;
	}

	public String getSOUND() {
		return SOUND;
	}

	public void setSOUND(String sOUND) {
		SOUND = sOUND;
	}
	public int getENABLE() {
		return ENABLE;
	}
	public void setENABLE(int eNABLE) {
		ENABLE = eNABLE;
	}
    public void setNextTime(){
    	String[] days = this.REPEAT.split(",");
    	ArrayList<String> daysArrayList = new ArrayList<String>(Arrays.asList(days));
    	
    	Calendar timeOff = new GregorianCalendar();
        Calendar curr =  new GregorianCalendar();
    	Calendar alarmtime = new GregorianCalendar();
    	alarmtime.setTimeInMillis(this.TIME);
        timeOff.set(Calendar.HOUR_OF_DAY, alarmtime.get(Calendar.HOUR_OF_DAY));
        timeOff.set(Calendar.MINUTE, alarmtime.get(Calendar.MINUTE));
        timeOff.set(Calendar.SECOND, 0);
        Log.i("TIME", alarmtime.get(Calendar.HOUR_OF_DAY)+" "+alarmtime.get(Calendar.MINUTE)+" "+alarmtime.get(Calendar.SECOND));
        Log.i("TIME", timeOff.get(Calendar.HOUR_OF_DAY)+" "+timeOff.get(Calendar.MINUTE)+" "+timeOff.get(Calendar.SECOND));
        int i = 0;
        while(i<8) {
        	if(daysArrayList.contains(numToDay[timeOff.get(Calendar.DAY_OF_WEEK)]) && !timeOff.before(curr)) {
        		//Set alarm and break because we have our alarm
        		alarmtime.set(Calendar.DAY_OF_WEEK, timeOff.get(Calendar.DAY_OF_WEEK));
            	Log.d("TIME RECORDED",alarmtime.get(Calendar.DAY_OF_WEEK)+"");
            	
        		break;
        	}
        	timeOff.add(Calendar.DAY_OF_YEAR, 1);
        	i++;
        }
        
    	Log.d("Current time",curr.getTimeInMillis()+"");
    	Log.i("Diff:",((timeOff.getTimeInMillis()-curr.getTimeInMillis()))/1000 +"");
    	
    	TIME = timeOff.getTimeInMillis();
    }
}