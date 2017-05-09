package com.example.sads.honeycontrol.service;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sads on 26/03/17.
 */
public class ApiAdapter {
    private static ApiService APISERVICE;

    public static ApiService getApiService(){
        String baseUrl = "http://192.168.1.65/HoneyControl/views/modules/";

        if(APISERVICE==null){

            OkHttpClient client = new OkHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            APISERVICE = retrofit.create(ApiService.class);

        }
        return APISERVICE;
    }
}
