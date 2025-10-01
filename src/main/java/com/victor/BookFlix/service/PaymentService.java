package com.victor.BookFlix.service;

import com.victor.BookFlix.dto.PaymentDTO;

import java.util.List;

public interface PaymentService {

    PaymentDTO processPayment(Long userId, Long subscriptionId, Double amount, String method);
    PaymentDTO getPaymentByReference(String reference);
    List<PaymentDTO> getUserPayments(Long userId);
}
