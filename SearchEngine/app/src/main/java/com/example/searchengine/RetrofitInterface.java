package com.example.searchengine;

import com.google.gson.JsonArray;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/DataResult")
    Call<JsonArray> executeData();

}
