package com.jrodiz.business.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("user")
    private String mUser;

    @SerializedName("password")
    private String mPassword;

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getUser() {
        return mUser;
    }

    public void setUser(String user) {
        mUser = user;
    }
}
