package com.example.quizzy;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public class Quiz {


    Date startAt;
    @SerializedName("createdAt")
    Date createdAt;

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @SerializedName("updatedAt")
    Date updatedAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<Response> getResponses() {
        return responses;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    @SerializedName("_id")
    String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }


    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }


    @SerializedName("access")
    String access;
    @SerializedName("rating")
    double rating;
    @SerializedName("difficulty")
    double difficulty;
    @SerializedName("owner")
    String owner;
    @SerializedName("title")
    String title;
    @SerializedName("password")
    String password;
    @SerializedName("duration")
    double duration;
    @SerializedName("startTime")
    long startTime;
    @SerializedName("tags")
    List<String> tags;
    @SerializedName("questions")
    List<Question> questions;
    @SerializedName("responses")
    List<Response> responses;

    public Quiz() {
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Quiz(String title, double duration, long startTime, List<Question> questions, List<String> tags, List<Response> responses) {
        this.tags = tags;
        this.title = title;
        this.duration = duration;
        this.startTime = startTime;
        this.questions = questions;
        this.responses = responses;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Quiz(Date startAt, Date createdAt, Date updatedAt, String _id, String owner, String title, String password, double duration,
                List<Question> questions, List<String> tags, List<Response> responses) {
        this.startAt = startAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this._id = _id;
        this.owner = owner;
        this.title = title;
        this.password = password;
        this.duration = duration;
        this.questions = questions;
        this.tags = tags;
        this.responses = responses;
    }

    public Quiz(String title, String password, double duration, long startTime, List<Question> questions, List<String> tags, List<Response> responses) {
        this.tags = tags;
        this.title = title;
        this.password = password;
        this.duration = duration;
        this.startTime = startTime;
        this.questions = questions;
        this.responses = responses;
    }

    public String getStr(){
        String str = title + "\n";
        for(int i = 0 ; i < tags.size() ; i++){
            str = str + tags.get(i) + " ";
        }
        str = str + "\n";
        str = str + getDifficulty() + "\n";
        str = str + getRating() + "\n";
        str = str + getOwner();
        return  str;
    }

    @NonNull
    @Override
    public String toString() {
        String str = title + "\n";
        for(int i = 0 ; i < tags.size() ; i++){
            str = str + tags.get(i) + " ";
        }
        str = str + "\n";
        for(int i = 0 ; i < questions.size() ; i++){
            str = str + questions.get(i).getDescription() + "\n";
        }
        return str;
    }
}
