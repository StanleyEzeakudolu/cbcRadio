package com.example.cbc_vcms_internal.controllers;

import com.example.cbc_vcms_internal.models.Analytics;
import com.example.cbc_vcms_internal.repositories.AnalyticsRepository;
import com.example.cbc_vcms_internal.utils.ResponseGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsRepository analyticsRepository;

    // Setter for analyticsRepository to enable injection during tests
    public void setAnalyticsRepository(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    @GetMapping("/getAnalytics")
    public ResponseEntity<?> getAnalytics(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String platform,
            @RequestParam(required = false) String postId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
            Page<Analytics> analyticsPage = analyticsRepository.findAll(pageable);

            List<Analytics> filteredAnalytics = analyticsPage.getContent().stream()
                    .filter(data -> platform == null || data.getPlatform().equalsIgnoreCase(platform))
                    .filter(data -> postId == null || data.getPostId().equals(postId))
                    .filter(data -> {
                        if (startDate == null && endDate == null)
                            return true;
                        LocalDateTime timestamp = data.getTimestamp();
                        LocalDateTime start = startDate != null ? LocalDateTime.parse(startDate) : null;
                        LocalDateTime end = endDate != null ? LocalDateTime.parse(endDate) : null;
                        return (start == null || !timestamp.isBefore(start))
                                && (end == null || !timestamp.isAfter(end));
                    })
                    .toList();

            if (filteredAnalytics.isEmpty()) {
                return ResponseGenerator.error(HttpStatus.NO_CONTENT, "No analytics data available.");
            }
            return ResponseGenerator.success("Analytics retrieved successfully.", filteredAnalytics);
        } catch (Exception e) {
            return ResponseGenerator.error(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch analytics.");
        }
    }

    @PostMapping("/saveAnalytics")
    public ResponseEntity<?> createAnalytics(@RequestBody Analytics analytics) {
        try {
            Analytics savedAnalytics = analyticsRepository.save(analytics);
            return ResponseGenerator.success("Analytics saved successfully.", savedAnalytics);
        } catch (Exception e) {
            return ResponseGenerator.error(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to save analytics.");
        }
    }
}
