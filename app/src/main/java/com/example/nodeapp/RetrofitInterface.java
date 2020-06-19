package com.example.nodeapp;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/save")
    Call<Void> executeInfo (@Body HashMap<String,String> hashMap);

}
