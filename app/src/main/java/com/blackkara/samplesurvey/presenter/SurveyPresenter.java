package com.blackkara.samplesurvey.presenter;

import android.util.Log;

import com.blackkara.samplesurvey.model.Survey;
import com.blackkara.samplesurvey.network.USayService;
import com.blackkara.samplesurvey.network.USayServiceApi;

import java.net.SocketTimeoutException;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SurveyPresenter {
    private static final String TAG = SurveyPresenter.class.getSimpleName();

    private static final String DEFAULT_SURVEY_IMG_RESOLUTION = Survey.RESOLUTION_LARGE;
    private static final int DEFAULT_SURVEY_LIST_PAGE = 1;
    private static final int DEFAULT_SURVEY_LIST_PER_PAGE = 30;

    private int mPage;
    private int mPerPage;
    private String mImgResolution;
    private USayServiceApi mUSayServiceApi;
    private Subscription mSurveyListSubscription;

    public SurveyPresenter(){
        mPage = DEFAULT_SURVEY_LIST_PAGE;
        mPerPage = DEFAULT_SURVEY_LIST_PER_PAGE;
        mImgResolution = DEFAULT_SURVEY_IMG_RESOLUTION;

        mUSayServiceApi = USayService.getInstance().getApi();

        loadSurveyList(false);
    }

    /**
     * Release all sources we use, so we don't need this presenter anymore
     */
    public void onDestroy(){
        unsubscribe();
    }


    public void loadSurveyList(){
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
            mSurveyListSubscription = mUSayServiceApi.getSurveyList(mPage, mPerPage)
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
                            Log.d(TAG, "Survey list is fetched, count : " + surveys.size());
                        }
                    });
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
}
