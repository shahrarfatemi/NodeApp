package com.example.quizzy;

import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("high")
    double high;
    @SerializedName("low")
    double low;
    @SerializedName("message")
    String message;
    String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Response(double high, double low, String message) {
        this.high = high;
        this.low = low;
        this.message = message;
    }
}
