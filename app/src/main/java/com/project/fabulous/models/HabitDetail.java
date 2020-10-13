package com.project.fabulous.models;

public class HabitDetail {
    private String description;
    private String content;

    public HabitDetail(String description, String content) {
        this.description = description;
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
