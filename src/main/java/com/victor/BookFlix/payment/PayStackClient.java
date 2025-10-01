package com.victor.BookFlix.payment;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class PayStackClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String PAYSTACK_SECRET = "sk_test_xxxxxxxxxxxxxxxxxxxxx"; // replace with your secret
    private static final String BASE_URL = "https://api.paystack.co";

    // Initialize Payment (no static)
    public String initializePayment(String email, Double amount) {
        String url = BASE_URL + "/transaction/initialize";

        // Request body
        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("amount", (int) (amount * 100)); // Paystack accepts amount in kobo (₦100 = 10000)

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(PAYSTACK_SECRET);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
            if (data != null) {
                return (String) data.get("authorization_url");
            }
        }
        throw new RuntimeException("Failed to initialize payment with Paystack");
    }

    // Verify Payment
    public boolean verifyPayment(String reference) {
        String url = BASE_URL + "/transaction/verify/" + reference;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(PAYSTACK_SECRET);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
            if (data != null) {
                String status = (String) data.get("status");
                return "success".equalsIgnoreCase(status);
            }
        }
        return false;
    }
}
