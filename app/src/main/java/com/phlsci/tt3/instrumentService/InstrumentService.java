/*
 *  Copyright (C) Philadelphia Science, Inc - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *  * Written by Timothy Tentinger <tim.tentinger@fynydd.com>, 4/6/17 7:12 PM
 *
 */

package com.phlsci.tt3.instrumentService;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class InstrumentService extends Service {
    public int counter=0;
    private String LOG_TAG = "InstrumentService";
    public InstrumentService(Context applicationContext) {
        super();
        Log.i(LOG_TAG, "Here I am!");
    }

    public InstrumentService() {

    }

    private ArrayList<String> mList;
    private boolean mRunning;   // Used to detect if service is already running

    @Override
    public void onCreate() {
        super.onCreate();
        mRunning = false;
        LOG_TAG = this.getClass().getSimpleName();
        Log.i(LOG_TAG, "In onCreate");
        mList = new ArrayList<String>();
        mList.add("Object 1");
        mList.add("Object 2");
        mList.add("Object 3");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOG_TAG, "Top onStartCommand");

        new Thread(new Runnable() {
            public void run() {

                if (!mRunning) {   // make sure not running already
                    mRunning = true;

                    Log.i(LOG_TAG, "Body of onStartCommand");

                    // need to check if below is correct place
                    startTimer();


                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent broadcastIntent = new Intent();
         //           broadcastIntent.setAction(MainActivity.mBroadcastStringAction);
                    broadcastIntent.putExtra("Data", "Broadcast Data");
                    sendBroadcast(broadcastIntent);

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
         //           broadcastIntent.setAction(MainActivity.mBroadcastIntegerAction);
                    broadcastIntent.putExtra("Data", 10);
                    sendBroadcast(broadcastIntent);

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
         //           broadcastIntent
         //                   .setAction(MainActivity.mBroadcastArrayListAction);
                    broadcastIntent.putExtra("Data", mList);
                    sendBroadcast(broadcastIntent);
                } else  // Just leave, service is already running.
                {
                    Log.i(LOG_TAG, "Service already running.");
                }
            }
        }).start();
        // Dont know yet which one to use look it up
        // return START_STICKY;
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Wont be called as service is not bound
        Log.i(LOG_TAG, "In onBind");
        return null;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.i(LOG_TAG, "In onTaskRemoved");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        /* below from example code
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent("uk.ac.shef.oak.ActivityRecognition.RestartSensor");
        sendBroadcast(broadcastIntent); */

        Log.i(LOG_TAG, "In onDestroy");
        stoptimertask();

    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  "+ (counter++));
            }
        };
    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }



}
