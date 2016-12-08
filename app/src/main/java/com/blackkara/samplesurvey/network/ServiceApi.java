package com.blackkara.samplesurvey.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

public class ServiceApi {
    private static final long TIMEOUT_CONN_IN_SECONDS = 15;
    private static final long TIMEOUT_READ_IN_SECONDS = 30;

    private static ServiceApi sServiceApi;

    private IServiceApi mIServiceApi;

    private ServiceApi(){}

    public static ServiceApi getInstance(){
        if (sServiceApi == null) {
            sServiceApi = new ServiceApi();
        }
        return sServiceApi;
    }

    public IServiceApi getApi(){
        if(mIServiceApi == null){
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.readTimeout(TIMEOUT_READ_IN_SECONDS, TimeUnit.SECONDS);
            httpClientBuilder.connectTimeout(TIMEOUT_CONN_IN_SECONDS, TimeUnit.SECONDS);
            httpClientBuilder.addInterceptor(new ApiAuthInterceptor(ApiAuth.getAuthToken()));

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiUrl.getBaseUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .client(httpClientBuilder.build())
                    .build();

            mIServiceApi =  retrofit.create(IServiceApi.class);
        }

        return mIServiceApi;
    }
}
