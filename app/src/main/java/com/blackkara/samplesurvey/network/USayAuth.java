package com.blackkara.samplesurvey.network;

import android.content.Context;

import com.blackkara.samplesurvey.R;

public class USayAuth {

    private static String sAuthToken;

    public static void init(Context context){
        sAuthToken = context.getString(R.string.access_token);
    }

    static String getAuthToken(){
        return sAuthToken;
    }
}

