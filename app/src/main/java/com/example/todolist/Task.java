package com.example.todolist;

import java.util.Date;

public class Task {
    private String id;
    private String userId;
    private String title;
    private String description;
    private boolean isCompleted;
    private Date createdAt;

    public Task() {

    }

    public Task(String id, String userId, String title, String description, boolean isCompleted, Date createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.isCompleted = isCompleted;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
