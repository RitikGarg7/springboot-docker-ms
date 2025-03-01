package com.example.apigateway.config;

import com.example.apigateway.filter.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AuthenticationFilter authenticationFilter) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/auth/login", "/auth/register").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION) // 🔥 Add AuthenticationFilter
                .build();
    }

}



//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AuthenticationFilter authenticationFilter) {
//        return http
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .authorizeExchange(exchanges -> exchanges
//                        .pathMatchers("/auth/login", "/auth/register").permitAll()
//                        .anyExchange().authenticated()
//                )
//                .addFilterBefore(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION) // ✅ Add the custom filter
//                .build();
//    }