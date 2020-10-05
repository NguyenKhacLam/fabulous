package com.project.fabulous.models;

public class DailyHabit {
    private String id;
    private String name;

    public DailyHabit(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
