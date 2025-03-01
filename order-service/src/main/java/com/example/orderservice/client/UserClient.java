package com.example.orderservice.client;

import com.example.orderservice.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")  // Service name registered in Eureka
public interface UserClient {

    @GetMapping("/auth/user")  // This should match user-service API
    UserResponse getUserByUsername(@RequestParam("username") String username);
}
