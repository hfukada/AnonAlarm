package com.app.anonalarm;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AlarmDatabase extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "AnonAlarmData";
 
    // AlarmItems table name
    private static final String TABLE_ALARMS = "anonAlarms";
 
    // AlarmItems Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_LABEL = "label";
    private static final String KEY_TIME = "time";
    private static final String KEY_REPEAT = "repeat";
    private static final String KEY_VIBRATE = "vibrate";
    private static final String KEY_VOLUME = "volume";
    private static final String KEY_FILTER = "filter";
    private static final String KEY_SNOOZE = "snooze";
    private static final String KEY_SOUND = "sound";
    private static final String KEY_ENABLE = "enable";
 
    public AlarmDatabase(Context context) {
    	super(context, DATABASE_NAME, null, DATABASE_VERSION);
        
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ALARM_ITEMS_TABLE = "CREATE TABLE " + TABLE_ALARMS+ "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LABEL + " TEXT,"
                + KEY_TIME + " INTEGER," + KEY_REPEAT + " INTEGER,"+KEY_VIBRATE+" INTEGER, "
                + KEY_VOLUME + " INTEGER," + KEY_FILTER + " TEXT," + KEY_SNOOZE + " TEXT," + KEY_SOUND + " TEXT," 
                + KEY_ENABLE + " INTEGER)";
        db.execSQL(CREATE_ALARM_ITEMS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);
 
        // Create tables again
        onCreate(db);
    }
    public void addAlarmItem(AlarmItem ai) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, ai.getID()); // alarmitem id
        values.put(KEY_LABEL, ai.getLABEL()); // alarmitem label
        values.put(KEY_TIME, ai.getTIME()); // alarmitem time
        values.put(KEY_REPEAT, ai.getREPEAT()); // alarmitem repeat
        values.put(KEY_VIBRATE, ai.getVIBRATE()); // alarmitem vibrate
        values.put(KEY_VOLUME, ai.getVOLUME()); // alarmitem volume
        values.put(KEY_FILTER, ai.getFILTER()); // alarmitem filter
        values.put(KEY_SNOOZE, ai.getSNOOZE()); // alarmitem snooze
        values.put(KEY_SOUND, ai.getSOUND()); // alarmitem sound
        Log.d("sql", ai.getFILTER());
        values.put(KEY_ENABLE, ai.getENABLE()); // alarmitem enable
     
        // Inserting Row
        db.insert(TABLE_ALARMS, null, values);
    }
    public AlarmItem getAlarmItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_ALARMS, new String[] { KEY_ID,KEY_LABEL, KEY_TIME, KEY_REPEAT,KEY_VIBRATE,
        		KEY_VOLUME, KEY_FILTER, KEY_SNOOZE, KEY_SOUND, KEY_ENABLE
        		}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        
        AlarmItem ai = new AlarmItem(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Long.parseLong(cursor.getString(2)),cursor.getString(3),
                Integer.parseInt(cursor.getString(4)),Integer.parseInt(cursor.getString(5)),
                cursor.getString(6),Integer.parseInt(cursor.getString(7)),cursor.getString(8),
                Integer.parseInt(cursor.getString(9)));
        cursor.close();
        return ai;
    }

	public ArrayList<AlarmItem> getAllAlarmItems() {
        ArrayList<AlarmItem> AlarmItemList = new ArrayList<AlarmItem>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ALARMS +" ORDER BY "+KEY_ID+ " DESC";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try{
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	                AlarmItem alarmItem = new AlarmItem(Integer.parseInt(cursor.getString(0)),
	                        cursor.getString(1), Long.parseLong(cursor.getString(2)),cursor.getString(3),
	                        Integer.parseInt(cursor.getString(4)),Integer.parseInt(cursor.getString(5)),
	                        cursor.getString(6),Integer.parseInt(cursor.getString(7)),cursor.getString(8),
	                        Integer.parseInt(cursor.getString(9)));
	                AlarmItemList.add(alarmItem);
	            } while (cursor.moveToNext());
	            
	        }
	        
        }catch (Exception e){
        	Log.e("Database error","Go Delete your database",e);
        	
        	
        }finally{
        	cursor.close();
        }
	        // return AlarmItem list
        return AlarmItemList;
    }
	public int updateAlarmItem(AlarmItem ai) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_ID, ai.getID());
        values.put(KEY_LABEL, ai.getLABEL()); // alarmitem label
        values.put(KEY_TIME, ai.getTIME()); // alarmitem time
        values.put(KEY_REPEAT, ai.getREPEAT()); // alarmitem repeat
        values.put(KEY_VIBRATE, ai.getVIBRATE()); // alarmitem vibrate
        values.put(KEY_VOLUME, ai.getVOLUME()); // alarmitem volume
        values.put(KEY_FILTER, ai.getFILTER()); // alarmitem filter
        values.put(KEY_SNOOZE, ai.getSNOOZE()); // alarmitem snooze
        values.put(KEY_SOUND, ai.getSOUND()); // alarmitem sound
        values.put(KEY_ENABLE, ai.getENABLE()); // alarmitem enable
 
        // updating row
        return db.update(TABLE_ALARMS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(ai.getID()) });
    }
 
    // Deleting single AlarmItem
    public void deleteAlarmItem(AlarmItem alarmitem) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARMS, KEY_ID + " = ?",
                new String[] { String.valueOf(alarmitem.getID()) });
        db.close();
    }
 
    // Getting AlarmItems Count
    public int getAlarmItemsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ALARMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();
        // return count
        return cursor.getCount();
    }
    public AlarmItem getClosest(){
    	String query = "SELECT * FROM " + TABLE_ALARMS + " ORDER BY " + KEY_TIME + " ASC";
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(query, null);
    	AlarmItem ai  = null;
    	if (cursor != null) {
            cursor.moveToFirst();
        
            ai = new AlarmItem(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), Long.parseLong(cursor.getString(2)),cursor.getString(3),
                    Integer.parseInt(cursor.getString(4)),Integer.parseInt(cursor.getString(5)),
                    cursor.getString(6),Integer.parseInt(cursor.getString(7)),cursor.getString(8),
                    Integer.parseInt(cursor.getString(9)));
        	Log.d("sql","got message");
    	}
    	cursor.close();
    	db.close();
    	if(ai==null) Log.e("sql", "no matches");
        return ai;
    }
    
}