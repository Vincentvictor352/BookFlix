package com.victor.BookFlix.service.impl;

import com.victor.BookFlix.dto.AnalyticsDTO;
import com.victor.BookFlix.repository.BookRepository;
import com.victor.BookFlix.repository.PaymentRepository;
import com.victor.BookFlix.repository.SubscriptionRepository;
import com.victor.BookFlix.repository.UserRepository;
import com.victor.BookFlix.service.AnalyticsService;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PaymentRepository paymentRepository;

    public AnalyticsServiceImpl(UserRepository userRepository,
                                BookRepository bookRepository,
                                SubscriptionRepository subscriptionRepository,
                                PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.paymentRepository = paymentRepository;
    }
    @Override
    public AnalyticsDTO getSummary() {
        return new AnalyticsDTO(
                getTotalUsers(),
                getActiveUsers(),
                getTotalBooks(),
                subscriptionRepository.count(),
                subscriptionRepository.countByActive(true),
                getTotalRevenue()
        );
    }

    @Override
    public long getTotalUsers() {
        return userRepository.count();
    }

    @Override
    public long getActiveUsers() {
        return userRepository.countByActive(true);
    }

    @Override
    public long getTotalBooks() {
        return bookRepository.count();
    }

    @Override
    public double getTotalRevenue() {
        return paymentRepository.sumSuccessfulPayments();

    }
}
