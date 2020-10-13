package com.project.fabulous.models;

public class FocusSession {
    private String userId;
    private int duration;
    private String createdAt;

    public FocusSession(String userId, int duration) {
        this.userId = userId;
        this.duration = duration;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public int getDuration() {
        return duration;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
