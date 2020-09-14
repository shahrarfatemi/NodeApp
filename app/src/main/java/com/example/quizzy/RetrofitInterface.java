package com.example.nodeapp;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/users")
    Call<Void> executeInfo (@Body HashMap<String,String> hashMap);

    @POST("/users/login")
    Call<Object> executeLogin (@Body HashMap<String,String> hashMap);

}
