package com.ecommerce.order.feign.fallback;

import com.ecommerce.commondto.payment.PaymentRequest;
import com.ecommerce.commondto.payment.PaymentResponse;
import com.ecommerce.commonexception.exception.ServiceUnavailableException;
import com.ecommerce.order.feign.PaymentFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentFeignClientFallbackFactory implements FallbackFactory<PaymentFeignClient> {
    @Override
    public PaymentFeignClient create(Throwable cause) {
        return new PaymentFeignClient() {

            @Override
            public PaymentResponse createPayment(PaymentRequest request) {
                log.error("[CircuitBreaker] payment-service unavailable — createPayment({}): {}",
                        request, cause.getMessage());

                throw new ServiceUnavailableException(
                        "Payment service is currently unavailable. Please try again later.");
            }

            @Override
            public PaymentResponse getPaymentByOrderId(Long orderId) {
                log.error("[CircuitBreaker] payment-service unavailable — getPaymentByOrderId({}): {}",
                        orderId, cause.getMessage());

                throw new ServiceUnavailableException(
                        "Payment service is currently unavailable. Please try again later.");
            }
        };
    }
}