package com.project.fabulous.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiBuilder {
    private static Api api;
    public static Api getInstance(){
        Gson gson = new GsonBuilder().setLenient().create();

        if (api == null){
//            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            api = new Retrofit.Builder()
                    .baseUrl("https://us-central1-fabulous-journey.cloudfunctions.net/api/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(Api.class);
        }
        return api;
    }
}
