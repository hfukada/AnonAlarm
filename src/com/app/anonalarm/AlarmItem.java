package com.app.anonalarm;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.util.Log;

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
    	long closest=0;
    	Calendar timeOff = new GregorianCalendar();
    	Calendar alarmtime = new GregorianCalendar();
    	Calendar curr = null;
    	alarmtime.setTimeInMillis(this.TIME);
    	for (int i = 0; i < days.length; i++){
    		if (days[i].equals("Mon")){
    			alarmtime.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
    		} else if (days[i].equals("Tue")){
    			alarmtime.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY);
    		} else if (days[i].equals("Wed")){
    			alarmtime.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
    		} else if (days[i].equals("Thu")){
    			alarmtime.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY);
    		} else if (days[i].equals("Fri")){
    			alarmtime.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
    		} else if (days[i].equals("Sat")){
    			alarmtime.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
    		} else{
    			alarmtime.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
    		}
	        //timeOff.set(Calendar.DAY_OF_WEEK, alarmtime.get(Calendar.DAY_OF_WEEK));
	        timeOff.set(Calendar.HOUR_OF_DAY, alarmtime.get(Calendar.HOUR_OF_DAY));
	        timeOff.set(Calendar.MINUTE, alarmtime.get(Calendar.MINUTE));
	        timeOff.set(Calendar.SECOND, 0);
	        curr =  Calendar.getInstance();
	        if (timeOff.before(curr)){
	        	timeOff.add(Calendar.DAY_OF_WEEK, 7);
	        }
	        if( i == 0 || closest > timeOff.getTimeInMillis()){
	        	closest = timeOff.getTimeInMillis();
	        }
    	}
    	Log.i("TIME SET",TIME+"");
    	Log.i("TIME CURR",curr.getTimeInMillis()+"");
    	TIME = closest;
    }
}