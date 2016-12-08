package com.blackkara.samplesurvey.network;

import android.content.Context;

import com.blackkara.samplesurvey.R;

public class ApiUrl {
    private static String sBaseUrl;

    public static void init(Context context){
        sBaseUrl = context.getString(R.string.base);
    }

    static String getBaseUrl(){
        return sBaseUrl;
    }
}
