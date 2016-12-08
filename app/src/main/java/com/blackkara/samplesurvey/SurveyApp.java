package com.blackkara.samplesurvey;

import android.app.Application;

import com.blackkara.samplesurvey.network.ApiAuth;
import com.blackkara.samplesurvey.network.ApiUrl;

public class SurveyApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        initUSayAuth();
        initUSayUrl();
    }

    private void initUSayAuth(){
        ApiAuth.init(getApplicationContext());
    }

    private void initUSayUrl(){
        ApiUrl.init(getApplicationContext());
    }
}
