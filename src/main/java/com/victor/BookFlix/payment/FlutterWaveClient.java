package com.victor.BookFlix.payment;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class FlutterWaveClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String FLW_SECRET = "FLWSECK_TEST-xxxxxxxxxxxxxxxxxxx-X";
    private final String BASE_URL = "https://api.flutterwave.com/v3";

    // ✅ Initialize payment
    public String initializePayment(String email, String phone, Double amount, String currency) {
        String url = BASE_URL + "/payments";

        Map<String, Object> body = new HashMap<>();
        body.put("tx_ref", "TX-" + System.currentTimeMillis());
        body.put("amount", amount);
        body.put("currency", currency);
        body.put("redirect_url", "https://yourdomain.com/payment/verify"); // callback URL
        body.put("customer", Map.of(
                "email", email,
                "phonenumber", phone,
                "name", email.split("@")[0]
        ));

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(FLW_SECRET);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
            if (data != null && data.containsKey("link")) {
                return (String) data.get("link"); // Flutterwave returns payment link
            }
        }
        throw new RuntimeException("Failed to initialize payment with Flutterwave");
    }

    // Verify payment
    public boolean verifyPayment(String transactionId) {
        String url = BASE_URL + "/transactions/" + transactionId + "/verify";

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setBearerAuth(FLW_SECRET);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
            if (data != null) {
                String status = (String) data.get("status");
                return "successful".equalsIgnoreCase(status);
            }
        }
        return false;
    }
}
