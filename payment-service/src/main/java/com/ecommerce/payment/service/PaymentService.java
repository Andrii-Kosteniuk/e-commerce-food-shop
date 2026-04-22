package com.ecommerce.payment.service;

import com.ecommerce.commondto.payment.PaymentRequest;
import com.ecommerce.commondto.payment.PaymentResponse;

public interface PaymentService {

    PaymentResponse createPayment(PaymentRequest request);
    PaymentResponse  confirmPayment(Long paymentId, Long userId);
    PaymentResponse getPaymentByOrderId(Long orderId);

}

