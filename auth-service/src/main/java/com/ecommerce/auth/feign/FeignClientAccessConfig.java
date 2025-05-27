package com.ecommerce.auth.feign;

import com.ecommerce.auth.jwt.JwtAuthenticationFilter;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FeignClientAccessConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            log.info("Adding Authorization header to Feign request");
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
                HttpServletRequest request = servletRequestAttributes.getRequest();
                String accessToken = jwtAuthenticationFilter.getAccessToken(request);
                log.info("Extracted access token: {}", accessToken);
                if (accessToken != null) {
                    requestTemplate.header("Authorization", "Bearer " + accessToken);
                }
                    log.info("Added Authorization header: ");

            }
        };
    }

}
