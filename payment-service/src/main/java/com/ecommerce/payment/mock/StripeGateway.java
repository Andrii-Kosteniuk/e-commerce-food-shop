package com.ecommerce.payment.mock;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

@Component
public class StripeGateway {

    public String processPayment(String currency, BigDecimal amount) {

        boolean result = new Random().nextBoolean();

        return result ? "ch_" + UUID.randomUUID().toString().substring(0, 25)
                : "";
    }
}
