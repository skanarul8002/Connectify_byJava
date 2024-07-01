package com.anarul.skwhatsapp.Models;

public class Status {
    private String statusId;
    private String imageUrl;
    private long timestamp;

    public Status() {
    }

    public Status(String statusId, String imageUrl, long timestamp) {
        this.statusId = statusId;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
