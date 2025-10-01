package com.victor.BookFlix.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentDTO {

    private Long id;
    private String reference;
    private String status;
    private Double amount;
    private String method;
    private LocalDateTime createdAt;
    private Long userId;
    private Long subscriptionId;
    private String paymentUrl;
}
