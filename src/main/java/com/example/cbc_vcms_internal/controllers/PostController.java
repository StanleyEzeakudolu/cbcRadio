package com.example.cbc_vcms_internal.controllers;

import com.example.cbc_vcms_internal.models.Post;
import com.example.cbc_vcms_internal.repositories.PostRepository;
import com.example.cbc_vcms_internal.services.social.SocialMediaService;
import com.example.cbc_vcms_internal.utils.ResponseGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private Map<String, SocialMediaService> socialMediaServices;

    @GetMapping("/getAllPosts")
    public ResponseEntity<?> getAllPosts(@RequestParam(required = false) String platform) {
        try {
            List<Post> posts;

            if (platform != null && !platform.isBlank()) {
                posts = postRepository.findAll().stream()
                        .filter(post -> post.getPlatforms() != null && post.getPlatforms().contains(platform.toLowerCase()))
                        .toList();
            } else {
                posts = postRepository.findAll();
            }

            if (posts.isEmpty()) {
                return ResponseGenerator.error(HttpStatus.NO_CONTENT, "No posts available.");
            }
            return ResponseGenerator.success("Posts retrieved successfully.", posts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseGenerator.error(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch posts.");
        }
    }

    @PostMapping("/pushPost")
    public ResponseEntity<?> createOrPostImmediately(@RequestBody Post post) {
        try {
            if (post.getPlatforms() == null || post.getPlatforms().isEmpty()) {
                return ResponseGenerator.error(HttpStatus.BAD_REQUEST, "At least one platform must be specified.");
            }

            if (post.getContent() == null || post.getContent().isBlank()) {
                return ResponseGenerator.error(HttpStatus.BAD_REQUEST, "Post content cannot be empty.");
            }

            if (post.getScheduledTime() == null) {
                post.getPlatforms().forEach(platform -> {
                    SocialMediaService service = socialMediaServices.get(platform.toLowerCase());
                    if (service != null) {
                        service.postContent(post.getContent());
                    }
                });
                post.setPosted(true);
                Post savedPost = postRepository.save(post);
                return ResponseGenerator.success("Post posted immediately.", savedPost);
            }

            if (post.getScheduledTime().isBefore(LocalDateTime.now())) {
                return ResponseGenerator.error(HttpStatus.BAD_REQUEST, "Scheduled time must be in the future.");
            }

            post.setPosted(false);
            Post savedPost = postRepository.save(post);
            return ResponseGenerator.success("Post scheduled successfully.", savedPost);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseGenerator.error(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create or post immediately.");
        }
    }

    @DeleteMapping("/deletePost/{id}")
    public ResponseEntity<?> deletePost(@PathVariable String id) {
        try {
            return postRepository.findById(id).map(post -> {
                postRepository.deleteById(id);
                return ResponseGenerator.success("Post deleted successfully.");
            }).orElse(ResponseGenerator.error(HttpStatus.NOT_FOUND, "Post with ID " + id + " not found."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseGenerator.error(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete post.");
        }
    }

    // Setters for testing
    public void setPostRepository(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void setSocialMediaServices(Map<String, SocialMediaService> socialMediaServices) {
        this.socialMediaServices = socialMediaServices;
    }
}
