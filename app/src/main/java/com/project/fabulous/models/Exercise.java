package com.project.fabulous.models;

public class Exercise {
    private int id;
    private String title;
    private String summary;

    public Exercise(int id, String title, String summary) {
        this.id = id;
        this.title = title;
        this.summary = summary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
