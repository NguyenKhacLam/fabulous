package com.project.fabulous.models;

public class Quotes {
    String id;
    String challenge;
    String author;

    public Quotes() {
    }

    public Quotes(String id, String challenge, String author) {
        this.id = id;
        this.challenge = challenge;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
