package com.project.fabulous.models;

public class HabitCategory {
    private int id;
    private String category;
    private String imgCategory;

    public HabitCategory(int id, String category) {
        this.id = id;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

//    public String getImgCategory() {
//        return imgCategory;
//    }
//
//    public void setImgCategory(String imgCategory) {
//        this.imgCategory = imgCategory;
//    }
}
