package com.victor.BookFlix.service;

import com.victor.BookFlix.dto.SubscriptionDTO;

import java.util.List;

public interface SubscriptionService {
    SubscriptionDTO createSubscription(Long userId, String plan, Double price, int durationInDays);
    List<SubscriptionDTO> getUserSubscriptions(Long userId);
    SubscriptionDTO getActiveSubscription(Long userId);
    void cancelSubscription(Long subscriptionId);
}
