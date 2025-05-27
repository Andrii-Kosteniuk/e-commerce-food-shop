package com.ecommerce.order.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class OrderSecurityConfig {

//    private final UserServiceClient userServiceClient;
//    private final FeignAuthClient authClient;

//    @Bean
//    public UserDetailsService userDetailsService() {
//        return username -> {
//            log.info("Calling user-service to retrieve user by email: {}", username);
//            UserResponse user = userServiceClient.getUserForLogin(username);
//            log.debug("User retrieved: {}", user);
//            if (user == null) {
//                throw new UsernameNotFoundException("User not found: " + username);
//            }
//
//            return authClient.getUserDetails(user);
//        };
//    }

//    @Bean
//    public SecurityFilterChain securityFilterChainOrder(HttpSecurity http) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().authenticated())
//                .build();
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setPasswordEncoder(passwordEncoder());
//        authProvider.setUserDetailsService(userDetailsService());
//        return authProvider;
//    }

}
