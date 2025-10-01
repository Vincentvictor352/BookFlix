package com.victor.BookFlix.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnalyticsDTO {

    private long totalUsers;
    private long activeUsers;
    private long totalBooks;
    private long totalSubscriptions;
    private long activeSubscriptions;
    private double totalRevenue;
}
