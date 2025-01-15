package com.example.cbc_vcms_internal.services.social;

import com.example.cbc_vcms_internal.models.Analytics;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

@Service
public class BlueskyService implements SocialMediaService {

    private final String BASE_URL = "https://api.bluesky.social/v1/posts";
    private final String BEARER_TOKEN = "YOUR_BEARER_TOKEN";

    @Override
    public String postContent(String content) {
        System.out.println("Simulating Bluesky API call...");
        String response = simulateApiCall(content);
        System.out.println("Bluesky Response: " + response);
        return response;
    }

    @Override
    public String postContentWithMedia(String content, String mediaUrl) {
        System.out.println("Simulating Bluesky API call with media...");
        String response = String.format(
                "{\"data\": {\"id\": \"bluesky12345\", \"text\": \"%s\", \"media\": \"%s\"}}",
                content, mediaUrl
        );
        System.out.println("Bluesky Response: " + response);
        return response;
    }

    @Override
    public Analytics fetchAnalytics(String postId) {
        System.out.println("Simulating fetching analytics for Bluesky post ID: " + postId);

        // Simulate random metrics
        Analytics analytics = new Analytics();
        analytics.setPostId(postId);
        analytics.setPlatform("bluesky");
        analytics.setImpressions((int) (Math.random() * 1000) + 50); // Random between 50 and 1050
        analytics.setLikes((int) (Math.random() * 600) + 30); // Random between 30 and 630
        analytics.setViews((int) (Math.random() * 2000) + 100); // Random between 100 and 2100
        analytics.setTimestamp(LocalDateTime.now());

        System.out.println("Simulated Analytics: " + analytics);
        return analytics;
    }

            // Use this for real API integration
    public String postBlueSkyReal(String content) {
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
        return String.format("{\"data\": {\"id\": \"bluesky12345\", \"text\": \"%s\"}}", content);
    }
}
