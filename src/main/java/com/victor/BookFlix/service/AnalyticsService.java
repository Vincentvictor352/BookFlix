package com.victor.BookFlix.service;

import com.victor.BookFlix.dto.AnalyticsDTO;

public interface AnalyticsService {
    AnalyticsDTO getSummary();
    long getTotalUsers();
    long getActiveUsers();
    long getTotalBooks();
    double getTotalRevenue();
}

