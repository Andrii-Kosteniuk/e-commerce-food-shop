package com.ecommerce.commondto.payment;

import jakarta.validation.constraints.NotNull;

public record ConfirmPaymentRequest(
        @NotNull(message = "Payment ID is required")
         Long paymentId
) {
}
