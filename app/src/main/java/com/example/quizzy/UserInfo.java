package com.example.quizzy;

import com.google.gson.annotations.SerializedName;

public class SaveInfo {
    @SerializedName("name")
    String name;
    @SerializedName("email")
    String email;
    @SerializedName("password")
    String password;


}
