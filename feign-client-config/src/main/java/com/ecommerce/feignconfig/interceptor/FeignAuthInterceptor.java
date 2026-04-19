package com.ecommerce.feignconfig.interceptor;

import com.ecommerce.security.filter.InternalAuthenticationFilter;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

public class FeignAuthInterceptor implements RequestInterceptor {

    @Value("${security.internal-api-key}")
    private String internalApiKey;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return;
        }

        requestTemplate.header(InternalAuthenticationFilter.HEADER_USER_EMAIL, authentication.getName());

        Object userId = authentication.getCredentials();
        if (userId != null) {
            requestTemplate.header(InternalAuthenticationFilter.HEADER_USER_ID, String.valueOf(userId));
        }

        Collection<?> authorities = authentication.getAuthorities();
        if (authorities != null && !authorities.isEmpty()) {
            String role = authorities.iterator().next().toString();
            requestTemplate.header(InternalAuthenticationFilter.HEADER_USER_ROLE, role);
        }

        requestTemplate.header(InternalAuthenticationFilter.HEADER_INTERNAL_API_KEY, internalApiKey);
    }
}
