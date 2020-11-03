package com.project.fabulous.models;

public class Blog {
    private String id;
    private String title;
    private String content;
    private String imageCover;
    private String viewCount;

    public Blog() {
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImageCover(String imageCover) {
        this.imageCover = imageCover;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageCover() {
        return imageCover;
    }
}
