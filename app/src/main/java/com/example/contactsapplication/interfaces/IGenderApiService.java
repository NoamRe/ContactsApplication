package com.example.contactsapplication.interfaces;

import com.example.contactsapplication.genderapi.GenderApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IGenderApiService {
    @GET("/")
    Call<GenderApiResponse> GetGenderInfo(@Query("name") String i_Name);
}
