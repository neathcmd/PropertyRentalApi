package com.rental.PropertyRentalApi.Service.Impl;

import com.rental.PropertyRentalApi.DTO.request.AuthRequest;
import com.rental.PropertyRentalApi.DTO.request.RegisterRequest;
import com.rental.PropertyRentalApi.DTO.response.AuthResponse;
import com.rental.PropertyRentalApi.DTO.response.RegisterResponse;
import com.rental.PropertyRentalApi.DTO.response.UserResponse;
import com.rental.PropertyRentalApi.Entity.UserEntity;
import com.rental.PropertyRentalApi.Repository.UserRepository;
import com.rental.PropertyRentalApi.Service.AuthService;
import com.rental.PropertyRentalApi.Service.JwtService;
import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.notFound;
import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.unauthorized;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return new RegisterResponse("Username already exists",null);
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new RegisterResponse("Email already exists", null);
        }

        UserEntity user = new UserEntity();
        user.setFullname(request.getFullname());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        UserEntity savedUser = userRepository.save(user);

        UserResponse userResponse = new UserResponse(
                savedUser.getId(),
                savedUser.getFullname(),
                savedUser.getUsername(),
                savedUser.getEmail()
        );

        return new RegisterResponse("Register successfully ", userResponse);
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> notFound("User not found."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw unauthorized("Invalid password");
        }

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getFullname(),
                user.getUsername(),
                user.getEmail()
        );

        String token = jwtService.generateToken(
                String.valueOf(user.getId()),
                user.getEmail(),
                user.getUsername()
                // user.getRole()
        );

        return new AuthResponse("Login successfully", userResponse, token);
    }


}