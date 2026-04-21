package com.ecommerce.payment.mock;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class StripeGateway {

    public String processPayment(String currency, BigDecimal amount) {

        return "ch_" + java.util.UUID.randomUUID().toString().substring(0, 24);
    }


}
