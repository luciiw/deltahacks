package com.example.fannydengdeng.showervolume;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.util.Log;

public class Background extends Service {

    public static final int SAMPLE_RATE = 16000;

    private AudioRecord mRecorder;
    private short[] mBuffer;
    String mode;
    int baseAmp;
    int newAmp;
    int volume_level;
    int baseVol;
    int difference;
    int iterations;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mode = (String)intent.getExtras().get("mode");
        initRecorder();
        mRecorder.startRecording();
        baseAmp = startBufferedWrite();
        mRecorder.release();
        mRecorder = null;
        mBuffer = null;
        Log.e("base amp", String.valueOf(baseAmp));

        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        baseVol= audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

       Log.e("vol level", String.valueOf(baseVol));

       try {
           Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (mode.equals("shower")) {
            while (baseVol != 0) {
              //AudioRecord recorder = null;
                initRecorder();
               // Log.e("init", "initialized");
                mRecorder.startRecording();
               // Log.e("start", "startRecording");
                newAmp = startBufferedWrite();
               // Log.e("start", "startBufferWrite");
                mRecorder.release();
                //Log.e("stop", "stopped");
                mRecorder = null;
                mBuffer = null;

                difference = newAmp - baseAmp;
                Log.e ("difference", String.valueOf(difference));


                volume_level= audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (difference > 0) {
                    iterations = (int) Math.round((double) difference / (2000.0+ 300*(volume_level-baseVol)));
                    Log.e ("iterations greater", String.valueOf(iterations));
                    for (int i = 1; i <= iterations; i++) {
                        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);

                        mBuffer = null;
                        mRecorder = null;
                       initRecorder();
                        Log.e("vol level", String.valueOf(volume_level));
                    }
                } else if (difference < 0) {
                    iterations = (int) Math.round((double) Math.abs(difference) / (2000.0+ 300*(volume_level-baseVol)));
                    Log.e ("iterations less", String.valueOf(iterations));
                    for (int i = 1; i <= iterations; i++) {
                        if (volume_level > baseVol) {
                            audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
                            volume_level = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                            mBuffer = null;
                            mRecorder = null;
                            initRecorder();
                            Log.e("vol level", String.valueOf(volume_level));
                        }
                    }

                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else if (mode.equals("street")) {
            while (baseVol != 0) {
                //AudioRecord recorder = null;
               // initRecorder();
                mRecorder.startRecording();
                newAmp = startBufferedWrite();
                mRecorder.release();
               // mRecorder = null;
              //  mBuffer = null;

                difference = newAmp - baseAmp;

                if (difference > 0) {
                    iterations = (int) Math.round((double) difference / (2000.0*(volume_level-baseVol)));
                    for (int i = 1; i <= iterations; i++) {
                        audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
                    }
                } else if (difference < 0) {
                    iterations = (int) Math.round((double) Math.abs(difference) / 2000.0);
                    for (int i = 1; i <= iterations; i++) {
                        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return START_STICKY;
    }

    private class OwnThread extends Thread {
        @Override
        public void run() {

        }
    }


    @Override
    public void onDestroy() {
        mRecorder.release();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initRecorder() {
        int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        mBuffer = new short[bufferSize];
        mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize);
    }

    private int startBufferedWrite() {

        /*new Thread(new Runnable() {
            @Override
            public int run() {*/
        double total = 0;
        // DataOutputStream output = null;
        int count= 0;
        //try {
        //  output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
        long start = System.currentTimeMillis();
        long end = start+5*1000;
        while (System.currentTimeMillis() < end) {
            double sum = 0;
            int readSize = Math.abs(mRecorder.read(mBuffer, 0, mBuffer.length));
            for (int i = 0; i < readSize; i++) {
                //   output.writeShort(mBuffer[i]);
                sum += mBuffer[i] * mBuffer[i];
            }
            if (readSize > 0) {
                final double amplitude = sum / readSize;
                //mProgressBar.setProgress((int) Math.sqrt(amplitude));
                total+= (Math.sqrt(amplitude));
                count++;
               // Log.e("vol", String.valueOf((int) Math.sqrt(amplitude)));
            }

            // Log.e("sum", String.valueOf(sum));
          //  Log.e("read size", String.valueOf(readSize));
        }
        return (int)total/count;

    }
}


