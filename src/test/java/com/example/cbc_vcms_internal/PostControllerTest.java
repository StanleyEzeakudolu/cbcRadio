package com.example.cbc_vcms_internal;

import com.example.cbc_vcms_internal.controllers.PostController;
import com.example.cbc_vcms_internal.models.Post;
import com.example.cbc_vcms_internal.repositories.PostRepository;
import com.example.cbc_vcms_internal.services.social.SocialMediaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PostRepository postRepository;

    @Mock
    private SocialMediaService twitterService;

    @InjectMocks
    private PostController postController;

    private Map<String, SocialMediaService> socialMediaServices;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        socialMediaServices = new HashMap<>();
        socialMediaServices.put("twitter", twitterService);
        postController.setSocialMediaServices(socialMediaServices);
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreatePost_ImmediateContentSuccess() throws Exception {
        Post post = new Post();
        post.setContent("Test content for immediate post");
        post.setPlatforms(List.of("twitter"));
        post.setPosted(true);

        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(twitterService.postContent("Test content for immediate post")).thenReturn("Simulated Twitter Response");

        mockMvc.perform(post("/api/v1/posts/pushContent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(post)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Post content posted immediately."))
                .andExpect(jsonPath("$.info.content").value("Test content for immediate post"))
                .andExpect(jsonPath("$.info.posted").value(true));
    }

    @Test
    public void testCreatePost_ImmediateMediaSuccess() throws Exception {
        Post post = new Post();
        post.setMediaUrl("https://example.com/media.jpg");
        post.setPlatforms(List.of("twitter"));
        post.setPosted(true);

        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(twitterService.postContentWithMedia(null, "https://example.com/media.jpg"))
                .thenReturn("Simulated Twitter Response with Media");

        mockMvc.perform(post("/api/v1/posts/pushMedia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(post)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Post media posted immediately."))
                .andExpect(jsonPath("$.info.mediaUrl").value("https://example.com/media.jpg"))
                .andExpect(jsonPath("$.info.posted").value(true));
    }

    @Test
    public void testCreatePost_ScheduledContentSuccess() throws Exception {
        Post post = new Post();
        post.setContent("Test content for scheduling");
        post.setPlatforms(List.of("twitter"));
        post.setScheduledTime(LocalDateTime.now().plusHours(1));
        post.setPosted(false);

        when(postRepository.save(any(Post.class))).thenReturn(post);

        mockMvc.perform(post("/api/v1/posts/pushContent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(post)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Post content scheduled successfully."))
                .andExpect(jsonPath("$.info.content").value("Test content for scheduling"))
                .andExpect(jsonPath("$.info.posted").value(false));
    }

    @Test
    public void testCreatePost_InvalidScheduledTime() throws Exception {
        Post post = new Post();
        post.setContent("This is content");
        post.setPlatforms(List.of("twitter"));
        post.setScheduledTime(LocalDateTime.now().minusHours(1));

        mockMvc.perform(post("/api/v1/posts/pushContent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(post)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Scheduled time must be in the future."));
    }

    @Test
    public void testCreatePost_MissingPlatforms() throws Exception {
        Post post = new Post();
        post.setContent("This is content");
        post.setScheduledTime(LocalDateTime.now().plusHours(1));

        mockMvc.perform(post("/api/v1/posts/pushContent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(post)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("At least one platform must be specified."));
    }

    @Test
    public void testCreatePost_BothContentAndMedia() throws Exception {
        Post post = new Post();
        post.setContent("Test content");
        post.setMediaUrl("https://example.com/media.jpg");
        post.setPlatforms(List.of("twitter"));
        post.setPosted(true);

        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(twitterService.postContentWithMedia("Test content", "https://example.com/media.jpg"))
                .thenReturn("Simulated Twitter Response with Content and Media");

        mockMvc.perform(post("/api/v1/posts/pushContentAndMedia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(post)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Post content and media posted immediately."))
                .andExpect(jsonPath("$.info.content").value("Test content"))
                .andExpect(jsonPath("$.info.mediaUrl").value("https://example.com/media.jpg"))
                .andExpect(jsonPath("$.info.posted").value(true));
    }
}
