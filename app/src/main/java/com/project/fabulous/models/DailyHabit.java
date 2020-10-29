package com.project.fabulous.models;

public class DailyHabit {
    private String id;
    private String name;
    private boolean todayStatus;

    public DailyHabit(String id, String name, boolean todayStatus) {
        this.id = id;
        this.name = name;
        this.todayStatus = todayStatus;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTodayStatus(boolean todayStatus) {
        this.todayStatus = todayStatus;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isTodayStatus() {
        return todayStatus;
    }
}
