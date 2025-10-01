package com.victor.BookFlix.controller;

import com.victor.BookFlix.dto.PaymentDTO;
import com.victor.BookFlix.entity.Payment;
import com.victor.BookFlix.payment.FlutterWaveClient;
import com.victor.BookFlix.payment.PayStackClient;
import com.victor.BookFlix.payment.PaypalClient;
import com.victor.BookFlix.payment.StripClient;
import com.victor.BookFlix.repository.PaymentRepository;
import com.victor.BookFlix.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;
    private final PayStackClient paystackClient;
    private final FlutterWaveClient flutterwaveClient;
    private final StripClient stripeClient;
    private final PaypalClient paypalClient;

    public PaymentController(PaymentService paymentService,
                             PaymentRepository paymentRepository,
                             PayStackClient paystackClient,
                             FlutterWaveClient flutterwaveClient,
                             StripClient stripeClient,
                             PaypalClient paypalClient) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
        this.paystackClient = paystackClient;
        this.flutterwaveClient = flutterwaveClient;
        this.stripeClient = stripeClient;
        this.paypalClient = paypalClient;
    }

    // Start payment
    @PostMapping("/process")
    public ResponseEntity<PaymentDTO> processPayment(
            @RequestParam Long userId,
            @RequestParam Long subscriptionId,
            @RequestParam Double amount,
            @RequestParam String method
    ) {
        PaymentDTO paymentDTO = paymentService.processPayment(userId, subscriptionId, amount, method);
        return ResponseEntity.ok(paymentDTO);
    }

    //  Paystack Webhook
    @PostMapping("/webhook/paystack")
    public ResponseEntity<String> handlePaystackWebhook(@RequestBody Map<String, Object> payload) {
        try {
            Map<String, Object> data = (Map<String, Object>) payload.get("data");
            String reference = (String) data.get("reference");

            boolean success = paystackClient.verifyPayment(reference);

            updatePaymentStatus(reference, success);
            return ResponseEntity.ok("Paystack webhook processed");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Paystack webhook error: " + e.getMessage());
        }
    }

    // Flutterwave Webhook
    @PostMapping("/webhook/flutterwave")
    public ResponseEntity<String> handleFlutterwaveWebhook(@RequestBody Map<String, Object> payload) {
        try {
            Map<String, Object> data = (Map<String, Object>) payload.get("data");
            String transactionId = String.valueOf(data.get("id"));
            String reference = (String) data.get("tx_ref");

            boolean success = flutterwaveClient.verifyPayment(transactionId);

            updatePaymentStatus(reference, success);
            return ResponseEntity.ok("Flutterwave webhook processed");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Flutterwave webhook error: " + e.getMessage());
        }
    }

    //  Stripe Webhook
    @PostMapping("/webhook/stripe")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody Map<String, Object> payload) {
        try {
            Map<String, Object> data = (Map<String, Object>) payload.get("data");
            String reference = (String) data.get("object"); // e.g. session_id or payment_intent

            boolean success = stripeClient.verifyPayment(reference);

            updatePaymentStatus(reference, success);
            return ResponseEntity.ok("Stripe webhook processed");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Stripe webhook error: " + e.getMessage());
        }
    }

    // PayPal Webhook
    @PostMapping("/webhook/paypal")
    public ResponseEntity<String> handlePayPalWebhook(@RequestBody Map<String, Object> payload) {
        try {
            String reference = (String) payload.get("resource_id");
            String status = (String) payload.get("status");

            boolean success = "COMPLETED".equalsIgnoreCase(status);
            updatePaymentStatus(reference, success);

            return ResponseEntity.ok("PayPal webhook processed");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("PayPal webhook error: " + e.getMessage());
        }
    }

    //  Utility method to update payment status
    private void updatePaymentStatus(String reference, boolean success) {
        Payment payment = paymentRepository.findByReference(reference);
        if (payment != null) {
            payment.setStatus(success ? "SUCCESS" : "FAILED");
            paymentRepository.save(payment);
        }
    }
}
