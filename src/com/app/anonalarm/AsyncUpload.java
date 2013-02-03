package com.app.anonalarm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Scanner;

import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

public class AsyncUpload extends AsyncTask<String, Integer, String>{
	private String filepath;
	void setFilePath(String filepath)
	{
		this.filepath = filepath;
	}
	
	@Override
	protected String doInBackground(String... params) {
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;

		Log.d("params", "params is"+filepath);
		String pathToOurFile = filepath;
		String urlServer = "http://deltatango.dyndns.org/upload.php";
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary =  "*****";
		

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1*1024*1024;

		try
		{
			FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile) );
	
			URL url = new URL(urlServer);
			connection = (HttpURLConnection) url.openConnection();
	
			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
	
			// Enable POST method
			connection.setRequestMethod("POST");
	        connection.setRequestProperty("Connection", "Keep-Alive");
	        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
			//connection.setRequestProperty("Content-Type", "multipart/form-data");
	        Log.d("checkpoint","1");
	        OutputStream os = connection.getOutputStream();
	        Log.d("checkpoint", "os");
			outputStream = new DataOutputStream( os );
			Log.d("checkpoint","2");
			String uid =  Settings.Secure.ANDROID_ID;
			
			//MessageDigest md = MessageDigest.getInstance("MD5");
			//md.digest(buffer, 0, maxBufferSize);
			
			String s2 = "3";
			Log.d("checkpoint","3");
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"uid\""+lineEnd+lineEnd+uid+lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"check\""+lineEnd+lineEnd+s2+lineEnd);
			String outFilePath [] = pathToOurFile.split("/");
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\""+outFilePath[outFilePath.length-1]+"\""+lineEnd+lineEnd);
			//outputStream.writeBytes("Content-Disposition: form-data; name=\""+s4+lineEnd);
			Log.d("checkpoint","4");
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
			Log.d("checkpoint","5");
			// Read file
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			Log.d("checkpoint","6");
			while (bytesRead > 0)
			{
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}
			fileInputStream.close();
			 outputStream.writeBytes(lineEnd + twoHyphens + boundary + lineEnd);
			outputStream.flush();
			outputStream.close();
			Log.d("checkpoint","7");
			// Responses from the server (code and message)
			int serverResponseCode = connection.getResponseCode();
			Log.d("checkpoint","8");
			String serverResponseMessage = connection.getResponseMessage();
			
			Log.d("serverResponseCode",Integer.toString(serverResponseCode));
			Log.d("serverResponseMessage",serverResponseMessage);
			InputStream in = connection.getInputStream();
			Scanner sc = new Scanner(in);
			while (sc.hasNext()) {
				Log.d("sc out",sc.nextLine());
			}
			
			Log.d("InputStream",in.toString());
			DataInputStream inStream = new DataInputStream(in);
			Log.d("InputStream-2",inStream.toString());
			
			
			
			
			}
			catch (Exception ex)
			{
			 Log.e("log", "error", ex);
			//Exception handling
			}
			
		 return null;
	}
}
