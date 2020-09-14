package com.example.quizzy;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserSubmissions {

    @SerializedName("userSubmissions")
    List<Submission> userSubmissions;

    public List<Submission> getUserSubmissions() {
        return userSubmissions;
    }

    public void setUserSubmissions(List<Submission> userSubmissions) {
        this.userSubmissions = userSubmissions;
    }

    public UserSubmissions(List<Submission> userSubmissions) {
        this.userSubmissions = userSubmissions;
    }
}
