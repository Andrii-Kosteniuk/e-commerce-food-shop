package com.ecommerce.order.feign.fallback;

import com.ecommerce.commonexception.exception.ServiceUnavailableException;
import com.ecommerce.order.feign.ProductFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductFeignClientFallbackFactory implements FallbackFactory<ProductFeignClient> {
    @Override
    public ProductFeignClient create(Throwable cause) {
        return id -> {
            log.error("[CircuitBreaker] product-service temporary unavailable — getProductById({}): {}",
                    id, cause.getMessage());
            throw new ServiceUnavailableException(
                    "Product service is currently unavailable. Please try again later.");
        };
    }
}
