package com.ecommerce.auth.feign;

import com.ecommerce.auth.feign.decoder.CustomFeignErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class FeignClientConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomFeignErrorDecoder();
    }
}
