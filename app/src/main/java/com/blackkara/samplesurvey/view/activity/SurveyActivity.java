package com.blackkara.samplesurvey.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blackkara.samplesurvey.R;
import com.blackkara.samplesurvey.model.Survey;
import com.blackkara.samplesurvey.presenter.SurveyPresenter;
import com.blackkara.samplesurvey.view.SurveyView;
import com.squareup.picasso.Picasso;

import eu.fiskur.simpleviewpager.ImageURLLoader;
import eu.fiskur.simpleviewpager.SimpleViewPager;


public class SurveyActivity extends AppCompatActivity implements SurveyView, ViewPager.OnPageChangeListener{
    private static final String TAG = SurveyActivity.class.getSimpleName();
    private static final String KEY_ONCE_STARTED = "ONCE_STARTED";

    private SurveyPresenter mPresenter;
    private SimpleViewPager mViewPager;
    private Button mButtonSurvey;
    private TextView mTextViewTitle;
    private TextView mTextViewDesc;
    private ProgressBar mProgressBar;
    private boolean mOnceStarted;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        if(savedInstanceState != null){
            mOnceStarted = savedInstanceState.getBoolean(KEY_ONCE_STARTED);
        }

        mButtonSurvey = (Button) findViewById(R.id.button_take_survey);
        mTextViewTitle = (TextView) findViewById(R.id.text_title);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar_load);
        mTextViewDesc = (TextView) findViewById(R.id.text_description);
        mViewPager = (SimpleViewPager) findViewById(R.id.pager_surveys);
        mViewPager.setOnPageChangeListener(this);
        mPresenter = new SurveyPresenter(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            Picasso.with(this).cancelTag(TAG);
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        mViewPager.clearListeners();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_ONCE_STARTED, true);
    }


    @Override
    public void loadImages(String[] urls) {
        mViewPager.setImageUrls(urls, new ImageURLLoader() {
            @Override
            public void loadImage(ImageView view, String url) {
                Picasso.with(SurveyActivity.this).load(url).tag(TAG).into(view);
            }
        });

        int indicatorColor = ContextCompat.getColor(this, R.color.white);
        int selectedIndicatorColor = ContextCompat.getColor(this, R.color.yellow);
        mViewPager.showIndicator(indicatorColor, selectedIndicatorColor);

        mTextViewTitle.setVisibility(View.VISIBLE);
        mTextViewDesc.setVisibility(View.VISIBLE);
        mButtonSurvey.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSurvey(Survey survey) {
        mTextViewTitle.setText(survey.getTitle());
        mTextViewDesc.setText(survey.getDescription());
    }

    @Override
    public void showProgress(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE: View.GONE);
    }

    @Override
    public boolean onceStarted() {
        return mOnceStarted;
    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public void onPageSelected(int position) {
        mPresenter.showSurvey(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageScrollStateChanged(int state) {}

    @SuppressWarnings("UnusedParameters")
    public void navigateClick(View view){
        Survey survey = mPresenter.getCurrentSurvey();
        SurveyDetailActivity.StartSurvey(this, survey);
    }

    @SuppressWarnings("UnusedParameters")
    public void refreshClick(View view){
        mPresenter.loadSurveyList();
    }
}
