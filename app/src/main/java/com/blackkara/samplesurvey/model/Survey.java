package com.blackkara.samplesurvey.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Survey {

    public static final String RESOLUTION_LARGE = "l";
    public static final String RESOLUTION_MEDIUM = "m";
    public static final String RESOLUTION_SMALL = "s";

    @SerializedName("title")
    private String mTitle;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("cover_image_url")
    private String mCoverImageUrl;

    public String getTitle(){
        return mTitle;
    }

    public void setTitle(String title){
        mTitle = title;
    }

    public String getDescription(){
        return mDescription;
    }

    public void setDescription(String description){
        mDescription = description;
    }

    public void setCoverImageUrl(String coverImageUrl){
        mCoverImageUrl = coverImageUrl;
    }

    public String getCoverImageUrl(){
        return mCoverImageUrl;
    }

    public String getCoverImageUrl(String resolution){
        String suffix = "";

        switch (resolution){
            case RESOLUTION_SMALL:
                suffix = RESOLUTION_SMALL;
                break;
            case RESOLUTION_MEDIUM:
                suffix = RESOLUTION_MEDIUM;
                break;
            case RESOLUTION_LARGE:
                suffix = RESOLUTION_LARGE;
                break;
        }

        return mCoverImageUrl + suffix;
    }

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Survey fromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Survey.class);
    }
}