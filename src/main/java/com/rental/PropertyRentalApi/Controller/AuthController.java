package com.rental.PropertyRentalApi.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rental.PropertyRentalApi.DTO.request.RegisterRequest;
import com.rental.PropertyRentalApi.DTO.response.RegisterResponse;
import com.rental.PropertyRentalApi.Service.AuthService;

import lombok.RequiredArgsConstructor;
import com.rental.PropertyRentalApi.DTO.request.AuthRequest;
import com.rental.PropertyRentalApi.DTO.response.AuthResponse;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        // Debugging: print the request to make sure it is received
        System.out.println("Received register request: " + request);

        // Call the service to register the user
        RegisterResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
