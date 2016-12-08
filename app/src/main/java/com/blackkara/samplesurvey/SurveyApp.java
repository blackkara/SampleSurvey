package com.blackkara.samplesurvey;

import android.app.Application;

import com.blackkara.samplesurvey.network.ServiceApi;

public class SurveyApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceApi.init(getApplicationContext());
    }
}
