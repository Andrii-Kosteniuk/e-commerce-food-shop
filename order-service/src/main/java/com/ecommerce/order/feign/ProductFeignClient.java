package com.ecommerce.order.feign;

import com.ecommerce.commondto.product.ProductResponse;
import com.ecommerce.feignconfig.FeignClientConfig;
import com.ecommerce.order.feign.fallback.ProductFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "product-service",
        path = "/api/v1/products",
        fallbackFactory = ProductFeignClientFallbackFactory.class)
public interface ProductFeignClient {

    @GetMapping("/{id}")
    ProductResponse getProductById(@PathVariable Long id);
}
