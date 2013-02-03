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
        	InputStream input = null;
        	synchronized(this) {
            URL url = new URL("http://24.1.186.151/get.php");
            URLConnection connection = url.openConnection();
            
            // download the file
            input = connection.getInputStream();
            String[] urlStr = connection.getURL().toString().split("/");

            filename = urlStr[urlStr.length-1];
            this.notifyAll();
        	}
            Log.d("url",filename);
            OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/AnonAlarmData/"+filename);

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
        return null;
    }
    public String getFilename(){
    	return filename;
    }

}