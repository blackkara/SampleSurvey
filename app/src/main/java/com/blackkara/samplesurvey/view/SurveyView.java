package com.blackkara.samplesurvey.view;

import android.content.Context;

import com.blackkara.samplesurvey.model.Survey;

public interface SurveyView {
    void loadImages(String[] urls);
    void showSurvey(Survey survey);
    void showProgress(boolean show);
    boolean onceStarted();
    Context getAppContext();
}
