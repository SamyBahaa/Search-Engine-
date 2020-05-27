package com.example.searchengine;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/DataResult")
    Call<JsonArray> executeData();

    @POST("/Number")
    Call<JsonElement> executeNumbers();


}
