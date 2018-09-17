package com.example.shaour.myapplicationnew;

import android.os.AsyncTask;

public class MyAsyncTask extends AsyncTask<String, Void, String>{

    private String name;

    public MyAsyncTask(String name){
        this.name = name;
    }

    @Override
    protected String doInBackground(String... params) {

        System.out.println(name+" is run "+System.currentTimeMillis()+" thread id "+Thread.currentThread().getId());

        try {
            Thread.sleep(1000*10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}