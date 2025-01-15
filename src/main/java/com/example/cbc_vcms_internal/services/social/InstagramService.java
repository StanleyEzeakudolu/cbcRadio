package com.example.cbc_vcms_internal.services.social;

import com.example.cbc_vcms_internal.models.Analytics;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

@Service
public class InstagramService implements SocialMediaService {

    private final String BASE_URL = "https://api.instagram.com/v1/posts";
    private final String BEARER_TOKEN = "YOUR_BEARER_TOKEN";

    @Override
    public String postContent(String content) {
        System.out.println("Simulating Instagram API call...");
        String response = simulateApiCall(content);
        System.out.println("Instagram Response: " + response);
        return response;
    }

    @Override
    public String postContentWithMedia(String content, String mediaUrl) {
        System.out.println("Simulating Instagram API call with media...");
        String response = String.format(
                "{\"data\": {\"id\": \"insta12345\", \"text\": \"%s\", \"media\": \"%s\"}}",
                content, mediaUrl
        );
        System.out.println("Instagram Response: " + response);
        return response;
    }

    @Override
    public Analytics fetchAnalytics(String postId) {
        System.out.println("Simulating fetching analytics for Instagram post ID: " + postId);

        // Simulate random metrics
        Analytics analytics = new Analytics();
        analytics.setPostId(postId);
        analytics.setPlatform("instagram");
        analytics.setImpressions((int) (Math.random() * 1500) + 200); // Random between 200 and 1700
        analytics.setLikes((int) (Math.random() * 800) + 100); // Random between 100 and 900
        analytics.setViews((int) (Math.random() * 3000) + 300); // Random between 300 and 3300
        analytics.setTimestamp(LocalDateTime.now());

        System.out.println("Simulated Analytics: " + analytics);
        return analytics;
    }

        // Use this for real API integration
    public String postInstagramReal(String content) {
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
        return String.format("{\"data\": {\"id\": \"insta12345\", \"text\": \"%s\"}}", content);
    }
}
