//package com.ecommerce.gatewaysecurity.config;
//
//import com.ecommerce.gatewaysecurity.filter.GatewayAuthenticationFilter;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class GatewaySecurityAutoConfiguration {
//
//    @Bean
//    @ConditionalOnMissingBean
//    public GatewayAuthenticationFilter gatewayAuthenticationFilter() {
//        return new GatewayAuthenticationFilter();
//    }
//}