package com.dot2dotz.app.Retrofit;


import com.dot2dotz.app.Helper.URLHelper;

import retrofit2.Retrofit;


public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static Retrofit retrofit_address = null;


    public static Retrofit getClient() {
        if (retrofit_address==null) {
            retrofit_address = new Retrofit.Builder()
                    .baseUrl(URLHelper.map_address_url)
                    .build();
        }
        return retrofit_address;
    }

    public static Retrofit getLiveTrackingClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URLHelper.base)
                    .build();
        }
        return retrofit;
    }
}
