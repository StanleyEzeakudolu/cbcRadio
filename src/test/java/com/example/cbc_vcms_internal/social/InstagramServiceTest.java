package com.example.cbc_vcms_internal.social;

import com.example.cbc_vcms_internal.models.Analytics;
import com.example.cbc_vcms_internal.services.social.InstagramService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InstagramServiceTest {

    private final InstagramService instagramService = new InstagramService();

    @Test
    public void testPostContent() {
        String content = "Hello from Instagram!";
        String response = instagramService.postContent(content);

        assertNotNull(response);
        assertTrue(response.contains("\"text\": \"" + content + "\""));
        System.out.println("Test passed: testPostContent");
    }

    @Test
    public void testPostContentWithMedia() {
        String content = "Hello with media on Instagram!";
        String mediaUrl = "https://example.com/insta.jpg";
        String response = instagramService.postContentWithMedia(content, mediaUrl);

        assertNotNull(response);
        assertTrue(response.contains("\"text\": \"" + content + "\""));
        assertTrue(response.contains("\"media\": \"" + mediaUrl + "\""));
        System.out.println("Test passed: testPostContentWithMedia");
    }

    @Test
    public void testFetchAnalytics() {
        String postId = "insta54321";
        Analytics analytics = instagramService.fetchAnalytics(postId);

        assertNotNull(analytics);
        assertEquals("instagram", analytics.getPlatform());
        assertEquals(postId, analytics.getPostId());
        assertTrue(analytics.getImpressions() >= 200 && analytics.getImpressions() <= 1700);
        assertTrue(analytics.getLikes() >= 100 && analytics.getLikes() <= 900);
        assertTrue(analytics.getViews() >= 300 && analytics.getViews() <= 3300);

        System.out.println("Test passed: testFetchAnalytics");
    }
}
