package com.ecommerce.order.feign;

import com.ecommerce.commondto.payment.PaymentRequest;
import com.ecommerce.commondto.payment.PaymentResponse;
import com.ecommerce.order.feign.fallback.PaymentFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "payment-service",
        path = "/api/v1/payments",
        fallbackFactory = PaymentFeignClientFallbackFactory.class)
public interface PaymentFeignClient {

    @PostMapping
    PaymentResponse createPayment(@RequestBody PaymentRequest request);

    @GetMapping("/order/{orderId}")
    PaymentResponse getPaymentByOrderId(@PathVariable Long orderId);
}
