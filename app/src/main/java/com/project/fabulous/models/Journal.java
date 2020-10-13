package com.project.fabulous.models;

public class Journal {
    private String date;
    private String titlePost;
    private String contentPost;

    public Journal(String date, String titlePost, String contentPost) {
        this.date = date;
        this.titlePost = titlePost;
        this.contentPost = contentPost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitlePost() {
        return titlePost;
    }

    public void setTitlePost(String titlePost) {
        this.titlePost = titlePost;
    }

    public String getContentPost() {
        return contentPost;
    }

    public void setContentPost(String contentPost) {
        this.contentPost = contentPost;
    }
}
