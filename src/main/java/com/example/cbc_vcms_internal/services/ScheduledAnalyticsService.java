package com.example.cbc_vcms_internal.services;

import com.example.cbc_vcms_internal.models.Analytics;
import com.example.cbc_vcms_internal.models.Post;
import com.example.cbc_vcms_internal.repositories.AnalyticsRepository;
import com.example.cbc_vcms_internal.repositories.PostRepository;
import com.example.cbc_vcms_internal.services.social.SocialMediaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Service
public class ScheduledAnalyticsService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AnalyticsRepository analyticsRepository;

    @Autowired
    private Map<String, SocialMediaService> socialMediaServices;

    private static final int MAX_RETRIES = 3;
    private static final int BATCH_SIZE = 100;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10); // Parallel threads

    @Scheduled(fixedRate = 3600000) // Runs every hour
    public void fetchAndStoreMetrics() {
        System.out.println("Starting batched analytics fetching...");

        List<Post> allPostedContent = postRepository.findAll().stream()
                .filter(Post::isPosted)
                .toList();

        int totalPosts = allPostedContent.size();
        if (totalPosts == 0) {
            System.out.println("No posted content found for analytics fetching.");
            return;
        }

        // Process in batches
        IntStream.range(0, (totalPosts + BATCH_SIZE - 1) / BATCH_SIZE)
                .mapToObj(i -> allPostedContent.subList(
                        i * BATCH_SIZE, Math.min((i + 1) * BATCH_SIZE, totalPosts)))
                .forEach(batch -> executorService.submit(() -> processBatch(batch)));

        System.out.println("Finished scheduling analytics jobs.");
    }

    private void processBatch(List<Post> batch) {
        batch.forEach(post -> post.getPlatforms().parallelStream().forEach(platform -> {
            SocialMediaService service = socialMediaServices.get(platform.toLowerCase());
            if (service != null) {
                fetchAnalyticsWithRetry(post, platform, service);
            } else {
                System.err.println("No service available for platform: " + platform);
            }
        }));
    }

    private void fetchAnalyticsWithRetry(Post post, String platform, SocialMediaService service) {
        int retryCount = 0;
        boolean success = false;

        while (!success && retryCount < MAX_RETRIES) {
            try {
                Analytics metrics = service.fetchAnalytics(post.getId());
                analyticsRepository.save(metrics);
                System.out.println("Saved metrics for post ID: " + post.getId() + " on platform: " + platform);
                success = true;
            } catch (Exception e) {
                retryCount++;
                System.err.println("Retrying fetch for post ID " + post.getId() +
                        " on platform " + platform + " (attempt " + retryCount + "): " + e.getMessage());
            }
        }

        if (!success) {
            System.err.println("Failed to fetch metrics for post ID " + post.getId() +
                    " on platform " + platform + " after " + MAX_RETRIES + " retries.");
        }
    }
}
