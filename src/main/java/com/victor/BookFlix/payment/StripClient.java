package com.victor.BookFlix.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StripClient {

    public StripClient(@Value("${stripe.secret.key}") String secretKey) {
        Stripe.apiKey = secretKey;
    }
    public boolean verifyPayment(String paymentIntentId) {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

            // Stripe considers payment successful when status = "succeeded"
            return "succeeded".equalsIgnoreCase(paymentIntent.getStatus());
        } catch (StripeException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String createCheckoutSession(String email, Double amount, String currency) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCustomerEmail(email)
                .setSuccessUrl("https://yourdomain.com/payment/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("https://yourdomain.com/payment/cancel")
                .addAllLineItem(
                        List.of(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(1L)
                                        .setPriceData(
                                                SessionCreateParams.LineItem.PriceData.builder()
                                                        .setCurrency(currency.toLowerCase())
                                                        .setUnitAmount((long) (amount * 100)) // amount in cents
                                                        .setProductData(
                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                        .setName("BookFlix Subscription")
                                                                        .build()
                                                        )
                                                        .build()
                                        )
                                        .build()
                        )
                )
                .build();

        Session session = Session.create(params);
        return session.getUrl(); // URL to redirect user to Stripe Checkout
    }
}
