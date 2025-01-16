package com.example.cbc_vcms_internal.models;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "analytics")
public class Analytics {

    @Id
    private String id;
    private String postId;
    private String platform;
    private int impressions;
    private int likes;
    private int views;
    private LocalDateTime timestamp;

    @CreatedDate
    private LocalDateTime createdTime;

    // Default Constructor
    public Analytics() {
    }

    // Constructor for easy creation
    public Analytics(String postId, String platform, int impressions, int likes, int views, LocalDateTime timestamp) {
        this.postId = postId;
        this.platform = platform;
        this.impressions = impressions;
        this.likes = likes;
        this.views = views;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPostId() {
        return postId;
    }
    public void setPostId(String postId) {
        this.postId = postId;
    }
    public String getPlatform() {
        return platform;
    }
    public void setPlatform(String platform) {
        this.platform = platform;
    }
    public int getImpressions() {
        return impressions;
    }
    public void setImpressions(int impressions) {
        this.impressions = impressions;
    }
    public int getLikes() {
        return likes;
    }
    public void setLikes(int likes) {
        this.likes = likes;
    }
    public int getViews() {
        return views;
    }
    public void setViews(int views) {
        this.views = views;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    // toString Method for Debugging
    @Override
    public String toString() {
        return String.format(
            "Analytics[postId=%s, platform=%s, impressions=%d, likes=%d, views=%d, timestamp=%s]",
            postId, platform, impressions, likes, views, timestamp
        );
    }
}
