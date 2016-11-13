package com.blackkara.samplesurvey.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

public class USayService {
    private static final long TIMEOUT_CONN_IN_SECONDS = 15;
    private static final long TIMEOUT_READ_IN_SECONDS = 30;

    private static USayService sUSayService;

    private USayServiceApi mUSayServiceApi;

    private USayService(){}

    public static USayService getInstance(){
        if (sUSayService == null) {
            sUSayService = new USayService();
        }
        return sUSayService;
    }

    public USayServiceApi getApi(){
        if(mUSayServiceApi == null){
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.readTimeout(TIMEOUT_READ_IN_SECONDS, TimeUnit.SECONDS);
            httpClientBuilder.connectTimeout(TIMEOUT_CONN_IN_SECONDS, TimeUnit.SECONDS);
            httpClientBuilder.addInterceptor(new USayAuthInterceptor(USayAuth.AUTHORIZATION));

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(USayUrl.BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .client(httpClientBuilder.build())
                    .build();

            mUSayServiceApi =  retrofit.create(USayServiceApi.class);
        }

        return mUSayServiceApi;
    }
}
