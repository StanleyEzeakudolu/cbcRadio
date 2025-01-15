package com.example.cbc_vcms_internal.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "posts")
public class Post {
    @Id
    private String id;
    private String content;
    private List<String> platforms;
    private LocalDateTime scheduledTime;
    private boolean isPosted;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public List<String> getPlatforms() {
        return platforms;
    }
    public void setPlatforms(List<String> platforms) {
        this.platforms = platforms;
    }
    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }
    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
    public boolean isPosted() {
        return isPosted;
    }
    public void setPosted(boolean isPosted) {
        this.isPosted = isPosted;
    }

    // Getters and Setters
    
   
}
