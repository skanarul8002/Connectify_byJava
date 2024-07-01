package com.anarul.skwhatsapp.Models;

public class Calls {
    private String userId;
    private String userName;
    private String date;
    private String urlProfile;
    private String callType;

    public Calls() {
    }

    public Calls(String userId, String userName, String date, String urlProfile, String callType) {
        this.userId = userId;
        this.userName = userName;
        this.date = date;
        this.urlProfile = urlProfile;
        this.callType = callType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrlProfile() {
        return urlProfile;
    }

    public void setUrlProfile(String urlProfile) {
        this.urlProfile = urlProfile;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }
}
