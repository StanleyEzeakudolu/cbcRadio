package com.example.cbc_vcms_internal.social;

import com.example.cbc_vcms_internal.models.Analytics;
import com.example.cbc_vcms_internal.services.social.TwitterService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TwitterServiceTest {

    private final TwitterService twitterService = new TwitterService();

    @Test
    public void testPostContent() {
        String content = "Hello from JUnit!";
        String response = twitterService.postContent(content);

        assertNotNull(response);
        assertTrue(response.contains("\"text\": \"" + content + "\""));
        System.out.println("Test passed: testPostContent");
    }

    @Test
    public void testPostContentWithMedia() {
        String content = "Hello with media!";
        String mediaUrl = "https://example.com/media.jpg";
        String response = twitterService.postContentWithMedia(content, mediaUrl);

        assertNotNull(response);
        assertTrue(response.contains("\"text\": \"" + content + "\""));
        assertTrue(response.contains("\"media\": \"" + mediaUrl + "\""));
        System.out.println("Test passed: testPostContentWithMedia");
    }

    @Test
    public void testFetchAnalytics() {
        String postId = "12345";
        Analytics analytics = twitterService.fetchAnalytics(postId);

        assertNotNull(analytics);
        assertEquals("twitter", analytics.getPlatform());
        assertEquals(postId, analytics.getPostId());
        assertTrue(analytics.getImpressions() >= 100 && analytics.getImpressions() <= 1100);
        assertTrue(analytics.getLikes() >= 50 && analytics.getLikes() <= 550);
        assertTrue(analytics.getViews() >= 200 && analytics.getViews() <= 2200);

        System.out.println("Test passed: testFetchAnalytics");
    }
}
