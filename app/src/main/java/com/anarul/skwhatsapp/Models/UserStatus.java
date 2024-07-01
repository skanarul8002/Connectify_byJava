package com.anarul.skwhatsapp.Models;

import java.util.ArrayList;

public class UserStatus {
    private String name,profile_img;
    private long last_update;
    private ArrayList<Status> statuses;

    public UserStatus() {
    }

    public UserStatus(String name, String profile_img, long last_update, ArrayList<Status> statuses) {
        this.name = name;
        this.profile_img = profile_img;
        this.last_update = last_update;
        this.statuses = statuses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public long getLast_update() {
        return last_update;
    }

    public void setLast_update(long last_update) {
        this.last_update = last_update;
    }

    public ArrayList<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<Status> statuses) {
        this.statuses = statuses;
    }
}
