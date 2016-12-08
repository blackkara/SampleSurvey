package com.blackkara.samplesurvey.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Implement token authentication
 * ApiAuthInterceptor integrates token and adds to header of each request
 * for Authorization value. Our http client passes this value automatically
 */
class ApiAuthInterceptor implements Interceptor {

    private String mAuthorization;

    ApiAuthInterceptor(String Authorization){
        mAuthorization = Authorization;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
                .header("Accept", "application/json")
                .header("Authorization", mAuthorization)
                .method(original.method(), original.body());

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}