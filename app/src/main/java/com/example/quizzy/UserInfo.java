package com.example.quizzy;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class UserInfo implements Serializable {

    public UserInfo(String name, String email, String token, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public UserInfo(String name, String email, String token) {
        this.name = name;
        this.email = email;
        this.token = token;
        this.password = "";
    }

    public UserInfo() {
    }

    String name;

    String email;

    String password;

    String _id;

    Date createdAt;

    Date updatedAt;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    String token;


}
