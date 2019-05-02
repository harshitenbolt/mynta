package com.canvascoders.opaper.api;

import com.canvascoders.opaper.activity.AppApplication;

import retrofit2.Retrofit;


public class ApiClient {
    // set your server n Api Base path here @ BASE URL

    public static Retrofit getClient() {
        return AppApplication.retrofit;
    }

    public static Retrofit getClient2() {
        return AppApplication.retrofit2;
    }

}
