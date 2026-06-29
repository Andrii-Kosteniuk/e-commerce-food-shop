package com.ecommerce.payment.stripe;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.UUID;

@Component
public class StripeGateway {
    private final SecureRandom secureRandom = new SecureRandom();

    public String processPayment() {

        byte [] bytes = new byte[20];
        secureRandom.nextBytes(bytes);

        return secureRandom.nextBoolean() ? "ch_" + UUID.randomUUID().toString().substring(0, 25)
                : "";
    }
}
