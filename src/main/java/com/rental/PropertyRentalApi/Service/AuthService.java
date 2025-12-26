package com.rental.PropertyRentalApi.Service;


import com.rental.PropertyRentalApi.Entity.UserEntity;
import com.rental.PropertyRentalApi.Repository.UserRepository;
import com.rental.PropertyRentalApi.DTO.AuthRequest;
import com.rental.PropertyRentalApi.DTO.AuthResponse;
import org.springframework.stereotype.Service;

@SuppressWarnings("hiding")
@Service
public class AuthService<AuthRequest> {

    public AuthService(UserRepository userRepository) {
    }
    public AuthResponse register(AuthRequest request) {
        // You can save user later. For now, just return dummy response
        return new AuthResponse(
                ((UserEntity) request).getUsername(),
                "Full Name Placeholder",
                "email@example.com",
                "dummy-token"
        );
    }
}

