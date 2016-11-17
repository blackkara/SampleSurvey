package com.blackkara.samplesurvey;

import android.app.Application;

import com.blackkara.samplesurvey.network.USayAuth;
import com.blackkara.samplesurvey.network.USayUrl;

public class SurveyApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        initUSayAuth();
        initUSayUrl();
    }

    private void initUSayAuth(){
        USayAuth.init(getApplicationContext());
    }

    private void initUSayUrl(){
        USayUrl.init(getApplicationContext());
    }
}
