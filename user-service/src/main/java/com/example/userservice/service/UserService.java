//package com.example.userservice.service;
//
//import com.example.userservice.entity.User;
//import com.example.userservice.repository.UserRepository;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class UserService {
//
//    private final UserRepository userRepository;
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    public UserService(UserRepository userRepository, KafkaTemplate kafkaTemplate) {
//        this.userRepository = userRepository;
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }
//
//    public User getUserById(Long id) {
//        return userRepository.findById(id).orElse(null);
//    }
//
//    public User createUser(User user) {
//        User savedUser = userRepository.save(user);
//        String auditMessage = "User created: " + savedUser.getId();
//        kafkaTemplate.send("audit-logs", auditMessage);
//        return savedUser;
//    }
//
//    public User updateUser(Long id, User user) {
//        if (userRepository.existsById(id)) {
//            user.setId(id);
//            return userRepository.save(user);
//        }
//        return null;
//    }
//
//    public void deleteUser(Long id) {
//        userRepository.deleteById(id);
//    }
//}


package com.example.userservice.service;

import com.example.userservice.entity.Role;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.util.JwtUtil;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, KafkaTemplate<String, String> kafkaTemplate, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash password
        if (user.getRole() == null) {
            user.setRole(Role.USER); // Default role
        }
        User savedUser = userRepository.save(user);
        kafkaTemplate.send("audit-logs", "User registered: " + savedUser.getId());
        return "User registered successfully!";
    }

    public String loginUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            User user = userOpt.get();
            return jwtUtil.generateToken(user); // ✅ Pass `User` object
        }
        return "Invalid credentials";
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
