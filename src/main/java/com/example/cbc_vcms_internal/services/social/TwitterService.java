package com.example.cbc_vcms_internal.services.social;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.cbc_vcms_internal.models.Analytics;

@Service
public class TwitterService implements SocialMediaService{

    private final String BASE_URL = "https://api.twitter.com/2/tweets";
    private final String BEARER_TOKEN = "YOUR_BEARER_TOKEN";

    @Override
    public String postContent(String content) {
        System.out.println("Simulating Twitter API call...");
        String response = simulateApiCall(content);
        System.out.println("Twitter Response: " + response);
        return response;
    }

    @Override
    public String postContentWithMedia(String content, String mediaUrl) {
        System.out.println("Simulating Twitter API call with media...");
        String response = String.format(
        "{\"data\": {\"id\": \"1234567890123456789\", \"text\": \"%s\", \"media\": \"%s\"}}",
        content, mediaUrl
    );
         System.out.println("Twitter Response: " + response);
         return response;
    }

    @Override
    public Analytics fetchAnalytics(String postId) {
        System.out.println("Simulating fetching analytics for Twitter post ID: " + postId);
    
        // Simulate random metrics
        Analytics analytics = new Analytics();
        analytics.setPostId(postId);
        analytics.setPlatform("twitter");
        analytics.setImpressions((int) (Math.random() * 1000) + 100); // Random between 100 and 1100
        analytics.setLikes((int) (Math.random() * 500) + 50); // Random between 50 and 550
        analytics.setViews((int) (Math.random() * 2000) + 200); // Random between 200 and 2200
        analytics.setTimestamp(LocalDateTime.now());
    
        System.out.println("Simulated Analytics: " + analytics);
        return analytics;
    }

    // Use this for real API integration
    public String postTweetReal(String content) {
        WebClient webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader("Authorization", "Bearer " + BEARER_TOKEN)
                .defaultHeader("Content-Type", "application/json")
                .build();

        // Request body
        String requestBody = String.format("{\"text\": \"%s\"}", content);

        return webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private String simulateApiCall(String content) {
        // Simulate a successful API call
        return String.format("{\"data\": {\"id\": \"1234567890123456789\", \"text\": \"%s\"}}", content);
    }
}
