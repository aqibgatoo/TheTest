package com.aqibgatoo.thetest.controller;

import android.app.Application;

import com.kinvey.android.Client;

/**
 * Created by Aqib on 12/17/2014.
 */
public class TestApplication extends Application {

    private static final String APP_KEY = "YOUR_APPKEY";
    private static final String APP_SECRET = "YOUR_APPSECRET";

    private static Client mKinveyClient;

    public TestApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mKinveyClient = new Client.Builder(APP_KEY, APP_SECRET, this.getApplicationContext()).build();


    }

    public static Client getInstance() {

        return mKinveyClient;

    }
}
