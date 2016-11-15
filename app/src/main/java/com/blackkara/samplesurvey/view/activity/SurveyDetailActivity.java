package com.blackkara.samplesurvey.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackkara.samplesurvey.R;
import com.blackkara.samplesurvey.model.Survey;
import com.squareup.picasso.Picasso;

public class SurveyDetailActivity extends AppCompatActivity {
    private static final String TAG = SurveyDetailActivity.class.getSimpleName();
    private static final String DEFAULT_SURVEY_IMG_RESOLUTION = Survey.RESOLUTION_MEDIUM;
    private static final String EXTRA_SURVEY = "EXTRA_SURVEY";


    public static void StartSurvey(Context context, Survey survey){
        String json = survey.toJson();
        Intent intent = new Intent(context, SurveyDetailActivity.class);
        intent.putExtra(EXTRA_SURVEY, json);
        context.startActivity(intent);
    }

    private String mImgResolution;
    private TextView mTextViewToolbarTitle;
    private TextView mTextViewTitle;
    private TextView mTextViewDesc;
    private ImageView mImageViewSurvey;
    private ImageButton mImageButtonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
        setContentView(R.layout.activity_survey_detail);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null || !bundle.containsKey(EXTRA_SURVEY)){
            finish();
        } else {
            Survey survey = Survey.fromJson(bundle.getString(EXTRA_SURVEY));
            init(survey);
        }
    }

    private void init(Survey survey){
        mImgResolution = DEFAULT_SURVEY_IMG_RESOLUTION;
        mTextViewTitle = (TextView) findViewById(R.id.text_title);
        mTextViewToolbarTitle = (TextView) findViewById(R.id.text_toolbar_title);
        mTextViewDesc = (TextView) findViewById(R.id.text_description);
        mImageViewSurvey = (ImageView) findViewById(R.id.image_survey);

        mImageButtonBack = (ImageButton) findViewById(R.id.imageButton_back);
        mImageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mTextViewTitle.setText(survey.getTitle());
        mTextViewDesc.setText(survey.getDescription());
        mTextViewToolbarTitle.setText(survey.getTitle());

        Picasso.with(SurveyDetailActivity.this)
                .load(survey.getCoverImageUrl(mImgResolution))
                .tag(TAG)
                .into(mImageViewSurvey);

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.hold, R.anim.push_out_to_left);
        if (isFinishing()) {
            Picasso.with(this).cancelTag(TAG);
        }
    }
}
