package com.example.quizzy;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnswerScript {

    @SerializedName("userSubmissions")
    List<Submission> userSubmissions;

    @SerializedName("incorrect")
    List<String> incorrect;

    @SerializedName("correct")
    List<String> correct;

    @SerializedName("_id")
    String _id;

    @SerializedName("quizId")
    String quizId;

    @SerializedName("userId")
    String userId;

    @SerializedName("marks")
    Double marks;

    public List<Submission> getUserSubmissions() {
        return userSubmissions;
    }

    public void setUserSubmissions(List<Submission> userSubmissions) {
        this.userSubmissions = userSubmissions;
    }

    public AnswerScript() {
    }

    public List<String> getIncorrect() {
        return incorrect;
    }

    public void setIncorrect(List<String> incorrect) {
        this.incorrect = incorrect;
    }

    public List<String> getCorrect() {
        return correct;
    }

    public void setCorrect(List<String> correct) {
        this.correct = correct;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getMarks() {
        return marks;
    }

    public void setMarks(Double marks) {
        this.marks = marks;
    }

    public AnswerScript(List<Submission> userSubmissions, List<String> incorrect,
                        List<String> correct, String _id, String quizId, String userId, Double marks) {
        this.userSubmissions = userSubmissions;
        this.incorrect = incorrect;
        this.correct = correct;
        this._id = _id;
        this.quizId = quizId;
        this.userId = userId;
        this.marks = marks;
    }
}
