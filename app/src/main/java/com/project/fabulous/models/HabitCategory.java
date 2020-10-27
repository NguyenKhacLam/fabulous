package com.project.fabulous.models;

public class HabitCategory {
    private String id;
    private String category;
    private String imgCategory;

    public HabitCategory(String id, String category) {
        this.id = id;
        this.category = category;
    }

    public HabitCategory() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImgCategory(String imgCategory) {
        this.imgCategory = imgCategory;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getImgCategory() {
        return imgCategory;
    }
}
