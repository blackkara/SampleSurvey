package com.blackkara.samplesurvey.network;

import com.blackkara.samplesurvey.model.Survey;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Interface for endpoint of USay api
 */
public interface IServiceApi {
    /**
     * Fetch surveys with server-side paging mechanism
     * @param page Indicates items of page temper to perPage value
     * @param perPage Item count for a page
     */
    @GET("/app/surveys")
    Observable<List<Survey>> getSurveyList(@Query("page") int page,
                                           @Query("per_page") int perPage);
}
