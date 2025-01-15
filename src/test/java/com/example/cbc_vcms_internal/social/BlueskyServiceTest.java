package com.example.cbc_vcms_internal.social;

import com.example.cbc_vcms_internal.models.Analytics;
import com.example.cbc_vcms_internal.services.social.BlueskyService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BlueskyServiceTest {

    private final BlueskyService blueskyService = new BlueskyService();

    @Test
    public void testPostContent() {
        String content = "Hello from Bluesky!";
        String response = blueskyService.postContent(content);

        assertNotNull(response);
        assertTrue(response.contains("\"text\": \"" + content + "\""));
        System.out.println("Test passed: testPostContent");
    }

    @Test
    public void testPostContentWithMedia() {
        String content = "Hello with media on Bluesky!";
        String mediaUrl = "https://example.com/bluesky.jpg";
        String response = blueskyService.postContentWithMedia(content, mediaUrl);

        assertNotNull(response);
        assertTrue(response.contains("\"text\": \"" + content + "\""));
        assertTrue(response.contains("\"media\": \"" + mediaUrl + "\""));
        System.out.println("Test passed: testPostContentWithMedia");
    }

    @Test
    public void testFetchAnalytics() {
        String postId = "bluesky98765";
        Analytics analytics = blueskyService.fetchAnalytics(postId);

        assertNotNull(analytics);
        assertEquals("bluesky", analytics.getPlatform());
        assertEquals(postId, analytics.getPostId());
        assertTrue(analytics.getImpressions() >= 50 && analytics.getImpressions() <= 1050);
        assertTrue(analytics.getLikes() >= 30 && analytics.getLikes() <= 630);
        assertTrue(analytics.getViews() >= 100 && analytics.getViews() <= 2100);

        System.out.println("Test passed: testFetchAnalytics");
    }
}
