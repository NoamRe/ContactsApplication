package com.example.contactsapplication.genderapi;

import com.example.contactsapplication.interfaces.IGenderApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit s_Retrofit;
    private static final String BASE_URL = "https://api.genderize.io";

    public synchronized static IGenderApiService GetGenderApiServiceInstance() {
        if (s_Retrofit == null) {
            s_Retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return s_Retrofit.create(IGenderApiService.class);
    }
}
