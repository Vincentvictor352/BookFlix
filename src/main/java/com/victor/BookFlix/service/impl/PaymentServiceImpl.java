package com.victor.BookFlix.service.impl;

import com.victor.BookFlix.dto.PaymentDTO;
import com.victor.BookFlix.entity.Payment;
import com.victor.BookFlix.entity.Subscription;
import com.victor.BookFlix.entity.User;
import com.victor.BookFlix.payment.FlutterWaveClient;
import com.victor.BookFlix.payment.PayStackClient;
import com.victor.BookFlix.payment.PaypalClient;
import com.victor.BookFlix.payment.StripClient;
import com.victor.BookFlix.repository.PaymentRepository;
import com.victor.BookFlix.repository.SubscriptionRepository;
import com.victor.BookFlix.repository.UserRepository;
import com.victor.BookFlix.service.PaymentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PayStackClient payStackClient;
    private final FlutterWaveClient flutterWaveClient;
    private final StripClient stripeClient;
    private final PaypalClient paypalClient;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              UserRepository userRepository,
                              SubscriptionRepository subscriptionRepository,
                              PayStackClient payStackClient,
                              FlutterWaveClient flutterWaveClient,
                              StripClient stripeClient,
                              PaypalClient paypalClient) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.payStackClient = payStackClient;
        this.flutterWaveClient = flutterWaveClient;
        this.stripeClient = stripeClient;
        this.paypalClient = paypalClient;
    }

    @Override
    public PaymentDTO getPaymentByReference(String reference) {
        Payment payment = paymentRepository.findByReference(reference);
        if (payment == null) {
            throw new RuntimeException("Payment not found with reference: " + reference);
        }
        return convertToDTO(payment);
    }

    @Override
    public List<PaymentDTO> getUserPayments(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return paymentRepository.findByUser(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentDTO processPayment(Long userId, Long subscriptionId, Double amount, String method) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        String paymentUrl;

        switch (method.toUpperCase()) {
            case "PAYSTACK":
                paymentUrl = payStackClient.initializePayment(user.getEmail(), amount);
                break;

            case "FLUTTERWAVE":
                paymentUrl = flutterWaveClient.initializePayment(user.getEmail(), "08012345678", amount, "NGN");
                break;

            case "STRIPE":
                try {
                    paymentUrl = stripeClient.createCheckoutSession(user.getEmail(), amount, "USD");
                } catch (Exception e) {
                    throw new RuntimeException("Stripe error: " + e.getMessage(), e);
                }
                break;

            case "PAYPAL":
                try {
                    paymentUrl = paypalClient.createPayment(amount, "USD", "BookFlix Subscription");
                } catch (Exception e) {
                    throw new RuntimeException("PayPal error: " + e.getMessage(), e);
                }
                break;

            default:
                throw new RuntimeException("Unsupported payment method: " + method);
        }

        Payment payment = new Payment();
        payment.setReference(UUID.randomUUID().toString());
        payment.setStatus("PENDING");
        payment.setAmount(amount);
        payment.setMethod(method.toUpperCase());
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUser(user);
        payment.setSubscription(subscription);

        Payment saved = paymentRepository.save(payment);

        PaymentDTO dto = convertToDTO(saved);
        dto.setPaymentUrl(paymentUrl); // ✅ clearly return the checkout/approval URL
        return dto;
    }

    // Converts entity -> DTO
    // Converts entity -> DTO
    private PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setReference(payment.getReference());
        dto.setStatus(payment.getStatus());
        dto.setAmount(payment.getAmount());
        dto.setMethod(payment.getMethod());
        dto.setCreatedAt(payment.getCreatedAt());

        if (payment.getUser() != null) {
            dto.setUserId(payment.getUser().getId());
        }
        if (payment.getSubscription() != null) {  //
            dto.setSubscriptionId(payment.getSubscription().getId());
        }
        return dto;
    }

}
