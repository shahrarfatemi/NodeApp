package com.example.quizzy;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Question {
    String _id;
    @SerializedName("description")
    String description;
    @SerializedName("answers")
    List<String> answers;
    @SerializedName("options")
    List<String> options;
    @SerializedName("type")
    String type;
    @SerializedName("marks")
    double marks;

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public Question() {
    }

    public Question(String description, List<String> answers, List<String> options, String type) {
        this.description = description;
        this.answers = answers;
        this.options = options;
        this.type = type;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
