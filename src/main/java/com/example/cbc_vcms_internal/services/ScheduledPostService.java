package com.example.cbc_vcms_internal.services;

import com.example.cbc_vcms_internal.models.Post;
import com.example.cbc_vcms_internal.repositories.PostRepository;
import com.example.cbc_vcms_internal.services.social.SocialMediaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ScheduledPostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private Map<String, SocialMediaService> socialMediaServices;

    @Scheduled(fixedRate = 60000) // Runs every 60 seconds
    public void postScheduledPosts() {
        // Find all due posts
        List<Post> duePosts = postRepository.findAll().stream()
                .filter(post -> post.getScheduledTime() != null &&
                                post.getScheduledTime().isBefore(LocalDateTime.now()) &&
                                !post.isPosted())
                .toList();

        if (duePosts.isEmpty()) {
            System.out.println("No posts to process.");
        } else {
            // Process each due post
            duePosts.forEach(post -> {
                System.out.println("Processing post: " + post.getContent());

                // Process the post for each platform
                post.getPlatforms().forEach(platform -> {
                    SocialMediaService service = socialMediaServices.get(platform.toLowerCase());
                    if (service != null) {
                        try {
                            String response = service.postContent(post.getContent());
                            System.out.println("Platform: " + platform + ", Response: " + response);
                        } catch (Exception e) {
                            System.err.println("Error posting to platform " + platform + ": " + e.getMessage());
                        }
                    } else {
                        System.out.println("No service available for platform: " + platform);
                    }
                });

                // Mark post as posted and save it
                post.setPosted(true);
                postRepository.save(post);
                System.out.println("Post marked as posted: " + post.getContent());
            });
        }

        System.out.println("Scheduler finished at: " + LocalDateTime.now());
    }
}
