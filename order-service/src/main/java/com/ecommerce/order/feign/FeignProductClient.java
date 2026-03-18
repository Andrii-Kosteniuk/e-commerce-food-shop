package com.ecommerce.order.feign;

import com.ecommerce.commondto.product.ProductResponse;
import com.study.feignclientconfig.feign.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", path = "/api/v1/products", configuration = FeignClientConfig.class )
public interface FeignProductClient {

    @GetMapping("/{id}")
    ProductResponse getProductById(@PathVariable Long id);
}
