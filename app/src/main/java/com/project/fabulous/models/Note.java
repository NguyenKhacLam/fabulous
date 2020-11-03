package com.project.fabulous.models;

import java.io.Serializable;

import javax.annotation.Nullable;

public class Note implements Serializable {
    private String id;
    private String title;
    private String subtitle;
    private String datetime;
    private String content;
    private String imageUrl;
    private String color;
    private String weblink;
    private String userId;

    public Note() {
    }

    public Note(String id, String title, String subtitle, String datetime, String content, String imageUrl, String color, String weblink, String userId) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.datetime = datetime;
        this.content = content;
        this.imageUrl = imageUrl;
        this.color = color;
        this.weblink = weblink;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return title + ":" + datetime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWeblink() {
        return weblink;
    }

    public void setWeblink(String weblink) {
        this.weblink = weblink;
    }
}
