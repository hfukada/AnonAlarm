package com.app.anonalarm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class RecordSide extends Activity{
	private static final int RECORDER_BPP = 16;
	private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
	private static final String AUDIO_RECORDER_FOLDER = "AnonAlarmData";
	private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
	private static final int RECORDER_SAMPLERATE = 44100;
	private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
	private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

	private Handler handler;
	private int num = 0;
	private int currentPlaying = 0;
	private AudioRecord recorder = null;
	private int bufferSize = 0;
	private Thread recordingThread = null;
	private boolean isRecording = false;
	private ListView lv;
	public MediaPlayer mPlayer;
	private ProgressBar pb;


	private ArrayList<String> listItems=new ArrayList<String>();
 	private ArrayAdapter<String> adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recordside);

		pb = (ProgressBar)findViewById(R.id.soundbar); // our progress bar received from layout

		setButtonHandlers();

		bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING);

		lv=(ListView)findViewById(R.id.list2);
		lv.setClickable(true);
		lv.setItemsCanFocus(false);
		lv.setLongClickable(true);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
				Object listItem = lv.getItemAtPosition(position);
				String filepath = Environment.getExternalStorageDirectory().getPath();
				File file = new File(filepath,AUDIO_RECORDER_FOLDER);
				playItem(file.getAbsolutePath() + "/"+listItem.toString());
			}
		});	
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				Object listItem = lv.getItemAtPosition(position);
				String filepath = Environment.getExternalStorageDirectory().getPath()+"/"+AUDIO_RECORDER_FOLDER+"/"+listItem.toString();
				Log.d("deleting",filepath);
				File file = new File(filepath);
				file.delete();
				listItems.remove(listItem);
				Toast.makeText(getApplicationContext(), "Deleted: "+listItem.toString(), Toast.LENGTH_SHORT).show();
				adapter.notifyDataSetChanged();
				return true;
			}
		});
		adapter=new ArrayAdapter<String>(this,R.layout.recording,R.id.recordinglabel,listItems);
		
		lv.setAdapter(adapter);
		
		File directory = new File(Environment.getExternalStorageDirectory().getPath(),AUDIO_RECORDER_FOLDER);
		
		if(directory.listFiles() != null){
			for (File file : directory.listFiles()){
				if (file.getName().contains("AnonRecording")){
					listItems.add(file.getName());
					num++;
				}
			}
		}
		adapter.notifyDataSetChanged();
		
	}

	private void setButtonHandlers() {
		((Button)findViewById(R.id.btnStart)).setOnClickListener(btnClick);
	}

	private String getFilename(){
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath,AUDIO_RECORDER_FOLDER);

		if(!file.exists()){
			file.mkdirs();
		}

		return (file.getAbsolutePath() + "/AnonRecording" + num + AUDIO_RECORDER_FILE_EXT_WAV);
	}

	private String getTempFilename(){
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath,AUDIO_RECORDER_FOLDER);

		if(!file.exists()){
			file.mkdirs();
		}

		File tempFile = new File(filepath,AUDIO_RECORDER_TEMP_FILE);

		if(tempFile.exists())
			tempFile.delete();

		return (file.getAbsolutePath() + "/" + AUDIO_RECORDER_TEMP_FILE);
	}

	private void startRecording(){

		if (mPlayer != null && mPlayer.isPlaying())
			mPlayer.release();
			mPlayer =null;
		recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,RECORDER_SAMPLERATE, RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING, bufferSize);

		recorder.startRecording();

		isRecording = true;

		recordingThread = new Thread(new Runnable() {

			@Override
			public void run() {
				writeAudioDataToFile();
			}
		},"AudioRecorder Thread");

		recordingThread.start();
		handler=new Handler();
		Runnable r=new Runnable() 
		{
			public void run() 
			{
				stopRecording();
			} 

		};
		handler.postDelayed(r, 15000);
	}

	private void writeAudioDataToFile(){
		byte data[] = new byte[bufferSize];
		String filename = getTempFilename();
		FileOutputStream os = null;
		int sum = 0;

		try {
			os = new FileOutputStream(filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int read = 0;

		if(null != os){
			while(isRecording){
				read = recorder.read(data, 0, bufferSize);
				sum=0;
				if(AudioRecord.ERROR_INVALID_OPERATION != read){
					try {
						os.write(data);
						//use data to see output
						for (int i = 0; i < read; i++) {
							sum += data [i] * data [i];
						}
						if (read > 0) {
							final double amplitude = sum / read;
							pb.setProgress((int) Math.sqrt(amplitude));
						}

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void stopRecording(){
		if(null != recorder){
			isRecording = false;

			recorder.stop();
			recorder.release();

			recorder = null;
			recordingThread = null;
			copyWaveFile(getTempFilename(),getFilename());

			deleteTempFile();
			addSoundToList();
			num++;
		}

	}

	private void deleteTempFile() {
		File file = new File(getTempFilename());

		file.delete();
	}

	private void copyWaveFile(String inFilename,String outFilename){
		FileInputStream in = null;
		FileOutputStream out = null;
		long totalAudioLen = 0;
		long totalDataLen = totalAudioLen + 36;
		long longSampleRate = RECORDER_SAMPLERATE;
		int channels = 2;
		long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * channels/8;

		byte[] data = new byte[bufferSize];

		try {
			in = new FileInputStream(inFilename);
			out = new FileOutputStream(outFilename);
			totalAudioLen = in.getChannel().size();
			totalDataLen = totalAudioLen + 36;

			//AppLog.logString("File size: " + totalDataLen);

			WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
					longSampleRate, channels, byteRate);

			while(in.read(data) != -1){
				out.write(data);
			}

			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void WriteWaveFileHeader(
			FileOutputStream out, long totalAudioLen,
			long totalDataLen, long longSampleRate, int channels,
			long byteRate) throws IOException {

		byte[] header = new byte[44];

		header[0] = 'R';  // RIFF/WAVE header
		header[1] = 'I';
		header[2] = 'F';
		header[3] = 'F';
		header[4] = (byte) (totalDataLen & 0xff);
		header[5] = (byte) ((totalDataLen >> 8) & 0xff);
		header[6] = (byte) ((totalDataLen >> 16) & 0xff);
		header[7] = (byte) ((totalDataLen >> 24) & 0xff);
		header[8] = 'W';
		header[9] = 'A';
		header[10] = 'V';
		header[11] = 'E';
		header[12] = 'f';  // 'fmt ' chunk
		header[13] = 'm';
		header[14] = 't';
		header[15] = ' ';
		header[16] = 16;  // 4 bytes: size of 'fmt ' chunk
		header[17] = 0;
		header[18] = 0;
		header[19] = 0;
		header[20] = 1;  // format = 1
		header[21] = 0;
		header[22] = (byte) channels;
		header[23] = 0;
		header[24] = (byte) (longSampleRate & 0xff);
		header[25] = (byte) ((longSampleRate >> 8) & 0xff);
		header[26] = (byte) ((longSampleRate >> 16) & 0xff);
		header[27] = (byte) ((longSampleRate >> 24) & 0xff);
		header[28] = (byte) (byteRate & 0xff);
		header[29] = (byte) ((byteRate >> 8) & 0xff);
		header[30] = (byte) ((byteRate >> 16) & 0xff);
		header[31] = (byte) ((byteRate >> 24) & 0xff);
		header[32] = (byte) (2 * 16 / 8);  // block align
		header[33] = 0;
		header[34] = RECORDER_BPP;  // bits per sample
		header[35] = 0;
		header[36] = 'd';
		header[37] = 'a';
		header[38] = 't';
		header[39] = 'a';
		header[40] = (byte) (totalAudioLen & 0xff);
		header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
		header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
		header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

		out.write(header, 0, 44);
	}

	private View.OnClickListener btnClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.btnStart:{
				//AppLog.logString("Start Recording");

				if(((Button)findViewById(R.id.btnStart)).getText().equals("Start")){
					Log.i("recording","Start");
					((Button)findViewById(R.id.btnStart)).setText("Stop");
					startRecording();
				} else {
					Log.i("recording","Stop");
					((Button)findViewById(R.id.btnStart)).setText("Start");
					stopRecording();
				}
				break;
			}
			}
		}
	};
	public void playItem(String filename){
		stopRecording();
		if (mPlayer != null && mPlayer.isPlaying())
			mPlayer.release();
			mPlayer =null;
		if (mPlayer == null)
			mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(filename);
			mPlayer.prepare();
			mPlayer.start();
		} catch (IOException e) {
			Log.e("Playing", e.toString());
		}

	}
	private void addSoundToList(){
		listItems.add("AnonRecording"+num+".wav");
		adapter.notifyDataSetChanged();
	}
}	