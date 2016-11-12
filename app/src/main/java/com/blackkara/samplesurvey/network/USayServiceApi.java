package com.blackkara.samplesurvey.network;

import com.blackkara.samplesurvey.model.Survey;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Interface for endpoint of USay api
 */
public interface USayServiceApi {
    /**
     * Fetch surveys with server-side paging mechanism
     * @param page Indicates items of page temper to perPage value
     * @param perPage Item count for a page
     */
    @GET(USayUrl.SURVEY_LIST)
    Observable<List<Survey>> getSurveyList(@Query(USayUrl.QUERY_PAGE) int page,
                                           @Query(USayUrl.QUERY_PER_PAGE) int perPage);
}
