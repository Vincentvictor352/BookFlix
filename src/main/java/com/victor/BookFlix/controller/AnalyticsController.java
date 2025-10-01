package com.victor.BookFlix.controller;

import com.victor.BookFlix.dto.AnalyticsDTO;
import com.victor.BookFlix.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/summary")
    public ResponseEntity<AnalyticsDTO> getSummary() {
        return ResponseEntity.ok(analyticsService.getSummary());
    }

    @GetMapping("/users/total")
    public ResponseEntity<Long> getTotalUsers() {
        return ResponseEntity.ok(analyticsService.getTotalUsers());
    }

    @GetMapping("/users/active")
    public ResponseEntity<Long> getActiveUsers() {
        return ResponseEntity.ok(analyticsService.getActiveUsers());
    }

    @GetMapping("/books/total")
    public ResponseEntity<Long> getTotalBooks() {
        return ResponseEntity.ok(analyticsService.getTotalBooks());
    }

    @GetMapping("/revenue/total")
    public ResponseEntity<Double> getTotalRevenue() {
        return ResponseEntity.ok(analyticsService.getTotalRevenue());
    }
}
