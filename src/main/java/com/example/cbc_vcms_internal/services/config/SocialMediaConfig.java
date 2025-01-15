package com.example.cbc_vcms_internal.services.config;

import com.example.cbc_vcms_internal.services.social.BlueskyService;
import com.example.cbc_vcms_internal.services.social.InstagramService;
import com.example.cbc_vcms_internal.services.social.TwitterService;
import com.example.cbc_vcms_internal.services.social.SocialMediaService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SocialMediaConfig {

    @Bean
    public Map<String, SocialMediaService> socialMediaServices(
            TwitterService twitterService,
            InstagramService instagramService,
            BlueskyService blueskyService) {
        Map<String, SocialMediaService> services = new HashMap<>();
        services.put("twitter", twitterService);
        services.put("instagram", instagramService);
        services.put("bluesky", blueskyService);
        return services;
    }
}
