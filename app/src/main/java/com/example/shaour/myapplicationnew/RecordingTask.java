package com.example.shaour.myapplicationnew;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

public class RecordingTask extends AsyncTask<String, Void, String> {

    private String name;
    private Recorder recorder;
    private AudioCalculator audioCalculator = new AudioCalculator();
    private Handler handler = new Handler(Looper.getMainLooper());
    public String fre;
    public String morsecode = "/";
    public boolean isDash;
    public boolean isdot;
    public boolean ispause;
    public boolean isbreak;
    public RecordingTask(){}

    @Override
    protected String doInBackground(String... params) {
        System.out.println("Start Recording.....");
        recorder = new Recorder(callback);
        recorder.start();

        try {
            Thread.sleep(1000*10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Callback callback = new Callback() {

        @Override
        public void onBufferAvailable(byte[] buffer) {
            audioCalculator.setBytes(buffer);
            if(audioCalculator.getFrequency()>10000)System.out.println(audioCalculator.getFrequency());
            final double frequency = audioCalculator.getFrequency();
            final String hz = String.valueOf(frequency + " Hz");
            final String val = String.valueOf("-");
            final String dot = String.valueOf(".");

            handler.post(new Runnable() {
                @Override
                public void run() {
                    MainActivity.getMainActivity().textFrequency.setText(hz);
                    // For Dot
                    if (frequency>11240 & frequency<11260) {
                        if (isdot == false) {
                            morsecode = morsecode + dot;
                            MainActivity.getMainActivity().textReturnValue.setText(morsecode);
                        }
                        isdot = true;
                    }else{
                        isdot = false;
                    }
                    // For Dash
                    if (frequency>14990 & frequency<15100) {
                        if (isDash == false) {
                            morsecode = morsecode  + val;
                            System.out.println(morsecode);
                            MainActivity.getMainActivity().textReturnValue.setText(morsecode);
                        }
                        isDash = true;
                    }else{
                        isDash = false;
                    }

                    if (frequency>12854 & frequency<12860) {
                        if (ispause == false) {
                            morsecode = morsecode+"//";
                            System.out.println(morsecode);
                            MainActivity.getMainActivity().textReturnValue.setText(morsecode);
                        }
                        ispause = true;
                    }else{
                        ispause = false;
                    }
                }
            });
        }
    };
}
