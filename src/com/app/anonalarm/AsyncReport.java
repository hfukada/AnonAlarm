package com.app.anonalarm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Scanner;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class AsyncReport extends AsyncTask {
	private String filepath;
	void setFilePath(String filepath)
	{
		this.filepath = filepath;
	}
	private String uid = "";
	void setUID(String uid)
	{
		this.uid = uid;
	}
	private String reportInput = "";
	void setUserMessage(String reportInput)
	{
		this.reportInput = reportInput;
	}
	@Override
	protected Object doInBackground(Object... params) {
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;

		Log.d("params", "params is"+filepath);
		String pathToOurFile = filepath;
		String urlServer = "http://deltatango.dyndns.org/report.php";
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary =  "*****";
		
		    
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1*1024*1024;

		try
		{	
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
			
			Log.d("checkpoint","3");
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
        	String outFilePath [] = pathToOurFile.split("/");
			outputStream.writeBytes("Content-Disposition: form-data; name=\"uid\""+lineEnd+lineEnd+md5(uid)+lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"filename\""+lineEnd+lineEnd+outFilePath[outFilePath.length-1]+lineEnd);		
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"reason\""+lineEnd+lineEnd+reportInput+lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"check\""+lineEnd+lineEnd+md5(reportInput+md5(uid)));
			//outputStream.writeBytes("Content-Disposition: form-data; name=\""+s4+lineEnd);
			Log.d("checkpoint","4");
			//Log.d("hash",md5(outFilePath[outFilePath.length-1]+md5(uid)));
			
			outputStream.writeBytes(lineEnd + twoHyphens + boundary + twoHyphens + lineEnd);
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
			while (sc.hasNextLine()) {
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

	public static String md5(String input) {
		try {
		String result = input;
		if(input!=null) {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(input.getBytes());
			BigInteger hash = new BigInteger(1, md.digest());
			result = hash.toString(16);
			while(result.length() < 32) {
				result = "0"+result;
			}
		}
		return result;
		}
		catch(Exception ex) {
		return input;
		}
	}

}