package com.app.anonalarm;

public class AlarmItem{
    private int ID;
    private String LABEL;
    private String TIME;
    private String REPEAT;
    private int VIBRATE;
    private int VOLUME;
    private String FILTER;
    private int SNOOZE;
    private String SOUND;
    private int ENABLE;
    
    public AlarmItem(int id, String label, String time, String repeat, int vibrate, int volume,
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

	public String getTIME() {
		return TIME;
	}

	public void setTIME(String tIME) {
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
    
}