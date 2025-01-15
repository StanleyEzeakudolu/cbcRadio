package com.example.cbc_vcms_internal;

import com.example.cbc_vcms_internal.controllers.AnalyticsController;
import com.example.cbc_vcms_internal.models.Analytics;
import com.example.cbc_vcms_internal.repositories.AnalyticsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AnalyticsControllerTest {

    private AnalyticsRepository analyticsRepository;
    private AnalyticsController analyticsController;

    @BeforeEach
    void setup() {
        analyticsRepository = Mockito.mock(AnalyticsRepository.class);
        analyticsController = new AnalyticsController();
        analyticsController.setAnalyticsRepository(analyticsRepository); // Inject the mock repository
    }

    @Test
    void testGetAnalytics() {
        Analytics analytics = new Analytics();
        analytics.setPostId("1");
        analytics.setPlatform("twitter");
        analytics.setViews(100);
        analytics.setTimestamp(LocalDateTime.now());

        List<Analytics> analyticsList = List.of(analytics);
        Page<Analytics> page = new PageImpl<>(analyticsList, PageRequest.of(0, 10), analyticsList.size());

        when(analyticsRepository.findAll(any(PageRequest.class))).thenReturn(page);

        ResponseEntity<?> response = analyticsController.getAnalytics(0, 10, "twitter", null, null, null);

        assertEquals(200, response.getStatusCode().value());
    }
}
