package com.project.fabulous.models;

import java.io.Serializable;

public class SubTask implements Serializable {
    private int id;
    private String title;
    private boolean status;

    public SubTask() {
    }

    public SubTask(int id, String title, boolean status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean getStatus() {
        return status;
    }
}
