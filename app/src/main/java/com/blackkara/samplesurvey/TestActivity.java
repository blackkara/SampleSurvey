package com.blackkara.samplesurvey;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.blackkara.samplesurvey.presenter.SurveyPresenter;

import java.util.Random;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = SurveyPresenter.class.getSimpleName();

    private static final int INVOKE_PERIOD_MIN_IN_MILLIS = 30 * 1000;
    private static final int INVOKE_PERIOD_MAX_IN_MILLIS = 50 * 1000;
    private static final int PERIOD_DIFFERENCE_IN_MILLIS = INVOKE_PERIOD_MAX_IN_MILLIS - INVOKE_PERIOD_MIN_IN_MILLIS;

    private int getRandomPeriod(){
        Random random = new Random();
        return random.nextInt(PERIOD_DIFFERENCE_IN_MILLIS) + INVOKE_PERIOD_MIN_IN_MILLIS;
    }

    private Handler mFakeInvoker = new Handler();

    private Runnable mInvokeRunnable = new Runnable() {
        @Override
        public void run() {
            mPresenter.loadSurveyList();
            int period = getRandomPeriod();
            mFakeInvoker.postDelayed(this, period);
            Log.d(TAG, "Invoke SurveyPresenter#loadSurveyList(), Next : " + period);
        }
    };

    private SurveyPresenter mPresenter;

    ImageView mPortrait1;
    ImageView mPortrait2;
    ImageView mPortrait3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mPortrait1 = (ImageView) findViewById(R.id.imageView1);
        mPortrait2 = (ImageView) findViewById(R.id.imageView2);
        mPortrait3 = (ImageView) findViewById(R.id.imageView3);

        mPresenter = new SurveyPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFakeInvoker.post(mInvokeRunnable);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        mFakeInvoker.removeCallbacks(mInvokeRunnable);
        super.onDestroy();
    }
}
