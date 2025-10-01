package com.victor.BookFlix.controller;


import com.victor.BookFlix.dto.SubscriptionDTO;
import com.victor.BookFlix.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/create")
    public ResponseEntity<SubscriptionDTO> createSubscription(@RequestParam Long userId,
                                                              @RequestParam String plan,
                                                              @RequestParam Double price,
                                                              @RequestParam int durationInDays) {
        return ResponseEntity.ok(subscriptionService.createSubscription(userId, plan, price, durationInDays));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SubscriptionDTO>> getUserSubscriptions(@PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionService.getUserSubscriptions(userId));
    }

    @GetMapping("/active/{userId}")
    public ResponseEntity<SubscriptionDTO> getActiveSubscription(@PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionService.getActiveSubscription(userId));
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelSubscription(@PathVariable Long id) {
        subscriptionService.cancelSubscription(id);
        return ResponseEntity.noContent().build();
    }

}
