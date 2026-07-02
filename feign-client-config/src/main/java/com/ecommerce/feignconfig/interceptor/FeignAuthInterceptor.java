package com.ecommerce.feignconfig.interceptor;

import com.ecommerce.security.filter.InternalAuthenticationFilter;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@RequiredArgsConstructor
public class FeignAuthInterceptor implements RequestInterceptor {

    private final String internalApiKey;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(InternalAuthenticationFilter.HEADER_INTERNAL_API_KEY, internalApiKey);

        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (requestAttributes != null) {

            HttpServletRequest req = requestAttributes.getRequest();

            String email = req.getHeader(InternalAuthenticationFilter.HEADER_USER_EMAIL);
            String userId = req.getHeader(InternalAuthenticationFilter.HEADER_USER_ID);
            String role = req.getHeader(InternalAuthenticationFilter.HEADER_USER_ROLE);

            if (email != null) requestTemplate.header(InternalAuthenticationFilter.HEADER_USER_EMAIL, email);
            if (userId != null) requestTemplate.header(InternalAuthenticationFilter.HEADER_USER_ID, userId);
            if (role != null) requestTemplate.header(InternalAuthenticationFilter.HEADER_USER_ROLE, role);
        }
    }
}
