package com.blackkara.samplesurvey;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.blackkara.samplesurvey.model.Survey;
import com.blackkara.samplesurvey.network.USayService;
import com.blackkara.samplesurvey.network.USayServiceApi;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";

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


        USayServiceApi aa = USayService.getInstance().getApi();
        Observable<List<Survey>> surveys = aa.getSurveyList(1, 20);

        Subscription mSurveySubscription = surveys
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Survey>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.toString());
                    }

                    @Override
                    public void onNext(List<Survey> surveys) {
                        Survey survey = surveys.get(new Random().nextInt(surveys.size() - 1));
                        Picasso.with(TestActivity.this).load(survey.getCoverImageUrl(Survey.RESOLUTION_SMALL)).into(mPortrait1);
                        Picasso.with(TestActivity.this).load(survey.getCoverImageUrl(Survey.RESOLUTION_MEDIUM)).into(mPortrait2);
                        Picasso.with(TestActivity.this).load(survey.getCoverImageUrl(Survey.RESOLUTION_LARGE)).into(mPortrait3);

                        for (Survey s: surveys) {
                            Log.d(TAG, "Title : " + s.getTitle() + ", Desc : " + s.getDescription() + ", Img : " + s.getCoverImageUrl());
                        }
                    }
                });
    }
}
