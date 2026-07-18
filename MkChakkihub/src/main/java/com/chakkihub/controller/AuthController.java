package com.chakkihub.controller;

import com.chakkihub.model.Customer;
import com.chakkihub.model.User;
import com.chakkihub.model.loginRequest.LoginRequest;
import com.chakkihub.model.loginResponse.LoginResponse;
import com.chakkihub.repository.CustomerRepository;
import com.chakkihub.repository.UserRepository;
import com.chakkihub.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired private UserRepository userRepo;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private CustomerRepository customerRepo;

    // Register as Admin
    @PostMapping("/register/admin")
    public User registerAdmin(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ADMIN");
        return userRepo.save(user);
    }

    // Register as Customer
    @PostMapping("/register/customer")
    public Customer registerCustomer(@RequestBody Customer customer) {
        User user = new User();
        user.setUsername(customer.getEmail());
        user.setPassword(passwordEncoder.encode("defaultPass")); // or from request
        user.setRole("CUSTOMER");
        userRepo.save(user);

        customer.setUser(user);
        return customerRepo.save(customer);
    }


    // Login (returns JWT)
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        User user = userRepo.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

            LoginResponse response = new LoginResponse();
            response.setToken(token);
            response.setUsername(user.getUsername());
            response.setRole(user.getRole());
            response.setUserId(user.getId());
            response.setEmail(user.getUsername()); // if username is email
            response.setLoginTime(java.time.LocalDateTime.now().toString());

            return response;
        }
        throw new RuntimeException("Invalid credentials");
    }

}
