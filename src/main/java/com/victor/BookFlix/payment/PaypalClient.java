package com.victor.BookFlix.payment;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PaypalClient {

    private final APIContext apiContext;

    public PaypalClient(
            @Value("${paypal.client.id}") String clientId,
            @Value("${paypal.client.secret}") String clientSecret,
            @Value("${paypal.mode}") String mode
    ) {
        this.apiContext = new APIContext(clientId, clientSecret, mode);
    }

    public String createPayment(Double amount, String currency, String description) throws PayPalRESTException {
        Amount amt = new Amount();
        amt.setCurrency(currency);
        amt.setTotal(String.format("%.2f", amount));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amt);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("https://yourdomain.com/payment/cancel");
        redirectUrls.setReturnUrl("https://yourdomain.com/payment/success");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        payment.setRedirectUrls(redirectUrls);

        Payment createdPayment = payment.create(apiContext);

        return createdPayment.getLinks().stream()
                .filter(link -> "approval_url".equals(link.getRel()))
                .findFirst()
                .map(com.paypal.api.payments.Links::getHref)
                .orElseThrow(() -> new RuntimeException("No PayPal approval link found"));
    }
}
