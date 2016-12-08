package com.blackkara.samplesurvey.presenter;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.blackkara.samplesurvey.model.Survey;
import com.blackkara.samplesurvey.network.ServiceApi;
import com.blackkara.samplesurvey.network.IServiceApi;
import com.blackkara.samplesurvey.view.SurveyView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SurveyPresenter {
    private static final String TAG = SurveyPresenter.class.getSimpleName();

    private static final String DEFAULT_SURVEY_IMG_RESOLUTION = Survey.RESOLUTION_MEDIUM;
    private static final int DEFAULT_SURVEY_LIST_PAGE = 2;
    private static final int DEFAULT_SURVEY_LIST_PER_PAGE = 10;

    private int mPage;
    private int mPerPage;
    private String mImgResolution;
    private int mCurrentSurveyPosition;
    private SurveyView mSurveyView;
    private IServiceApi mIServiceApi;
    private Subscription mSurveyListSubscription;
    private List<Survey> mSurveys = new ArrayList<>();

    public SurveyPresenter(SurveyView view){
        mSurveyView = view;
        mCurrentSurveyPosition = -1;
        mPage = DEFAULT_SURVEY_LIST_PAGE;
        mPerPage = DEFAULT_SURVEY_LIST_PER_PAGE;
        mImgResolution = DEFAULT_SURVEY_IMG_RESOLUTION;

        mIServiceApi = ServiceApi.getInstance().getApi();

        mSurveyView.showProgress(true);

        if(mSurveyView.onceStarted()){
            loadFromCache();
            if(mSurveys.size() == 0){
                loadSurveyList(false);

            }
        } else {
            loadSurveyList(false);
        }
    }

    /**
     * Release all sources we use, so we don't need this presenter anymore
     */
    public void onDestroy(){
        mSurveyView = null;
        unsubscribe();
    }


    public void loadSurveyList(){
        mCurrentSurveyPosition = -1;
        mSurveyView.showProgress(true);
        loadSurveyList(true);
    }

    /**
     * Register a subscription to survey list
     * @param isForced In normal way, survey list is loaded when presenter initialized
     *                 firs time, so it's not forced. But later, if Activity needs (user clicks
     *                 refresh button) to invoke this again, this method will be forced !
     */
    private void loadSurveyList(boolean isForced){
        if(mSurveyListSubscription == null || mSurveyListSubscription.isUnsubscribed() || isForced){
            if (mSurveyListSubscription != null && !mSurveyListSubscription.isUnsubscribed()) {
                mSurveyListSubscription.unsubscribe();
                Log.d(TAG, "Previous subscription has been cancelled");
            }

            Log.d(TAG, "Subscribed to survey list. Page : " + mPage + " Per page : " + mPerPage);
            mSurveyListSubscription = mIServiceApi.getSurveyList(mPage, mPerPage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<Survey>>() {
                        @Override
                        public void onCompleted() {

                        }
                        @Override
                        public void onError(Throwable t) {
                            handleError(t);
                        }
                        @Override
                        public void onNext(List<Survey> surveys) {
                            cacheSurveys(surveys);
                            handleSurveys(surveys);
                            Log.d(TAG, "Survey list is fetched, count : " + surveys.size());
                        }
                    });
        }
    }

    private void handleSurveys(List<Survey> surveys){
        mSurveys = surveys;
        if(mSurveys.size() > 0){
            int size = surveys.size();
            String[] urlArray = new String[size];
            for (int i = 0; i < surveys.size(); i++) {
                urlArray[i] = surveys.get(i).getCoverImageUrl(mImgResolution);
            }
            mSurveyView.showProgress(false);
            mSurveyView.loadImages(urlArray);
            mCurrentSurveyPosition = 0;
            showSurvey(0);
        }
    }

    /**
     * Handle network errors
     * Case SocketTimeoutException : What happens has timed out ?
     * Cut in half the load e.g mPerPage = mPerPage / 2;
     */
    private void handleError(Throwable t){
        Log.d(TAG, t.getMessage());
        if (t instanceof SocketTimeoutException) {
            mPerPage = mPerPage / 2;
            loadSurveyList(true);
        }
    }

    /**
     * Remove subscription to prevent possible leaks
     */
    private void unsubscribe(){
        if(mSurveyListSubscription != null){
            mSurveyListSubscription.unsubscribe();
            Log.d(TAG, "Survey list unsubscribed");
        }
    }

    public void showSurvey(int idx){
        if(mSurveys.size() > 0){
            mCurrentSurveyPosition = idx;
            mSurveyView.showSurvey(mSurveys.get(idx));
        }
    }

    public Survey getCurrentSurvey(){
        return mSurveys.get(mCurrentSurveyPosition);
    }

    private void cacheSurveys(List<Survey> surveys){
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(mSurveyView.getAppContext());

        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(surveys);

        editor.putString(TAG, json);
        editor.apply();
    }

    private void loadFromCache(){
        List<Survey> surveys = new ArrayList<>();

        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(mSurveyView.getAppContext());

        if(sharedPrefs.contains(TAG)){
            Gson gson = new Gson();
            String json = sharedPrefs.getString(TAG, null);
            Type type = new TypeToken<ArrayList<Survey>>() {}.getType();
            surveys =  gson.fromJson(json, type);
        }

        handleSurveys(surveys);
    }
}
