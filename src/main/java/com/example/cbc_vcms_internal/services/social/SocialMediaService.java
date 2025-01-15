package com.example.cbc_vcms_internal.services.social;

import com.example.cbc_vcms_internal.models.Analytics;

public interface SocialMediaService {
    /**
     * Post content to the platform.
     * @param content The content to be posted.
     * @return A string response (simulated or real).
     */
    String postContent(String content);

    /**
     * Post content with media to the platform.
     * @param content The text content.
     * @param mediaUrl The media URL (image, video, etc.).
     * @return A string response (simulated or real).
     */
    String postContentWithMedia(String content, String mediaUrl);

    Analytics fetchAnalytics(String postId);
}
