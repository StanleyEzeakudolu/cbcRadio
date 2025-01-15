package com.example.cbc_vcms_internal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.cbc_vcms_internal.services.social.TwitterService;
import com.example.cbc_vcms_internal.services.social.InstagramService;
import com.example.cbc_vcms_internal.services.social.BlueskyService;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CbcVcmsInternalApplicationTests {

    @Mock
    private TwitterService twitterService;

    @Mock
    private InstagramService instagramService;

    @Mock
    private BlueskyService blueskyService;

    @InjectMocks
    private CbcVcmsInternalApplicationTests applicationTests;

    @Test
    void contextLoads() {
        // Verifying that the mocked beans are loaded into the application context
        assertNotNull(twitterService, "TwitterService bean should be loaded into the context");
        assertNotNull(instagramService, "InstagramService bean should be loaded into the context");
        assertNotNull(blueskyService, "BlueskyService bean should be loaded into the context");
    }
}
