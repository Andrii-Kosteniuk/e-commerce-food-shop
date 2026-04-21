package com.ecommerce.order.feign;

import com.ecommerce.commondto.payment.PaymentRequest;
import com.ecommerce.commondto.payment.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "payment-service",
        path = "/api/v1/payments")
public interface PaymentFeignClient {

    @PostMapping("/create")
    ResponseEntity<PaymentResponse> createPayment(PaymentRequest request);

}
