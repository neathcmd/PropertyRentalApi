package com.rental.PropertyRentalApi.Service.Impl;

import com.rental.PropertyRentalApi.DTO.request.RegisterRequest;
import com.rental.PropertyRentalApi.DTO.response.RegisterResponse;
import com.rental.PropertyRentalApi.DTO.request.AuthRequest;
import com.rental.PropertyRentalApi.DTO.response.AuthResponse;
import com.rental.PropertyRentalApi.DTO.response.UserResponse;
import com.rental.PropertyRentalApi.Entity.UserEntity;
import com.rental.PropertyRentalApi.Repository.UserRepository;
import com.rental.PropertyRentalApi.Service.AuthService;
import com.rental.PropertyRentalApi.Utils.CookieHelper;

import com.rental.PropertyRentalApi.DTO.response.ApiResponse;
import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.badRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CookieHelper cookieHelper;

    @Override
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw badRequest("Username already exists");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw badRequest("Email already exists");
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

        return new ApiResponse<>(
                201,
                "Register successfully.",
                new RegisterResponse(userResponse)
                ).getData();
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        return null;
    }


    @Override
    public ApiResponse<Object> logout(HttpServletRequest request, HttpServletResponse response) {

        String accessToken = cookieHelper.getCookieValue(request, "accessToken");
        String refreshToken = cookieHelper.getCookieValue(request, "refreshToken");

        cookieHelper.clearAuthCookie(response, "accessToken");
        cookieHelper.clearAuthCookie(response, "refreshToken");

        return new ApiResponse<>("User logout successfully.");
    }
}
