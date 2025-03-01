package com.example.apigateway.filter;

import com.example.apigateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
@Component
public class AuthenticationFilter implements WebFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Allow public endpoints without authentication
        if (request.getURI().getPath().contains("/auth/login") || request.getURI().getPath().contains("/auth/register")) {
            return chain.filter(exchange);
        }

        // Extract Authorization header
        List<String> authHeaders = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (authHeaders == null || authHeaders.isEmpty() || !authHeaders.get(0).startsWith("Bearer ")) {
            return handleUnauthorized(exchange.getResponse());
        }

        String token = authHeaders.get(0).substring(7);
        try {
            Claims claims = jwtUtil.extractClaims(token);
            String username = claims.getSubject();
            String role = claims.get("roles", String.class);

            System.out.println("✅ Valid Token for user: " + username + " with roles: " + role);

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);


            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    username, null, Collections.singletonList(authority)
            );

            SecurityContext securityContext = new SecurityContextImpl(auth);

            // Forward JWT claims to downstream services
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("Authorization", "Bearer " + token)
                    .header("X-Authenticated-User", username)
                    .header("X-User-Roles", role)  // Keep it as a string
                    .build();

            ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();

            return chain.filter(modifiedExchange)
                    .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));

        } catch (Exception e) {
            System.out.println("🚨 Token validation failed: " + e.getMessage());
            return handleUnauthorized(exchange.getResponse());
        }
    }

    private Mono<Void> handleUnauthorized(ServerHttpResponse response) {
        response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }
}
