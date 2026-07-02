package com.ecommerce.feignconfig;


import com.ecommerce.feignconfig.decoder.CustomFeignErrorDecoder;
import com.ecommerce.feignconfig.interceptor.FeignAuthInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomFeignErrorDecoder();
    }

    @Bean
    public FeignAuthInterceptor feignAuthInterceptor(@Value("${security.internal-api-key}") String internalApiKey) {
        return new FeignAuthInterceptor(internalApiKey);
    }

}
