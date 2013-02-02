package com.app.anonalarm;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

class DownloadSound extends AsyncTask<String, Integer, String> {
	private String filename;
    protected String doInBackground(String... params) {
    	int timeint=0;
        try {
            URL url = new URL("http://24.1.186.151/get.php");
            URLConnection connection = url.openConnection();
            connection.connect();
            // this will be useful so that you can show a typical 0-100% progress bar
            int fileLength = connection.getContentLength();

            // download the file
            timeint = (int)System.currentTimeMillis();
            InputStream input = new BufferedInputStream(url.openStream());
            Log.d("url",connection.getURL().toString());
            OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/AnonAlarmData/"+ timeint +".mp3");

            byte data[] = new byte[1024];
            int count;
            while ((count = input.read(data)) != -1) {
                // publishing the progress....
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
        	Log.e("ST",e.getClass().getName(),e);
        	Log.d("Download", e.getMessage());
        }
        filename = timeint+".mp3";
        return null;
    }
    public String getFilename(){
    	return filename;
    }

}