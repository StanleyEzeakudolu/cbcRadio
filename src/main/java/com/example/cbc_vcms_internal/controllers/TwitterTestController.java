package com.example.cbc_vcms_internal.controllers;

import com.example.cbc_vcms_internal.services.social.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TwitterTestController {

    @Autowired
    private TwitterService twitterService;

    // Endpoint to test posting plain text
    @GetMapping("/test-twitter-post")
    public String testTwitterPost(@RequestParam String content) {
        return twitterService.postContent(content);
    }

    // Endpoint to test posting with media
    @GetMapping("/test-twitter-post-media")
    public String testTwitterPostWithMedia(@RequestParam String content, @RequestParam String mediaUrl) {
        return twitterService.postContentWithMedia(content, mediaUrl);
    }
}
