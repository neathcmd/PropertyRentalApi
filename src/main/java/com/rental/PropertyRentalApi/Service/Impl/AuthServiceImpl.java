package com.rental.PropertyRentalApi.Service.Impl;

import com.rental.PropertyRentalApi.DTO.request.RegisterRequest;
import com.rental.PropertyRentalApi.DTO.response.RegisterResponse;
import com.rental.PropertyRentalApi.DTO.request.AuthRequest;
import com.rental.PropertyRentalApi.DTO.response.AuthResponse;
import com.rental.PropertyRentalApi.DTO.response.UserResponse;
import com.rental.PropertyRentalApi.DTO.response.ApiResponse;
import com.rental.PropertyRentalApi.Entity.UserEntity;
import com.rental.PropertyRentalApi.Repository.UserRepository;
import com.rental.PropertyRentalApi.Service.AuthService;
import com.rental.PropertyRentalApi.Service.JwtService;
import com.rental.PropertyRentalApi.Utils.CookieHelper;


import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.notFound;
import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.unauthorized;
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
    private final JwtService jwtService;
    private final CookieHelper cookieHelper;

    @Override
    public RegisterResponse register(
            RegisterRequest request,
            HttpServletResponse response
    ) {

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

        // ========================
        // GENERATE TOKENS
        // ========================
        String accessToken = jwtService.generateAccessToken(
                String.valueOf(savedUser.getId()),
                savedUser.getEmail(),
                savedUser.getUsername()
        );

        String refreshToken = jwtService.generateRefreshToken(
                String.valueOf(savedUser.getId())
        );

        // ========================
        // SAVE TOKENS IN COOKIE
        // ========================
        cookieHelper.setAuthCookie(
                response,
                "accessToken",
                accessToken,
                300 // 5 minutes
        );

        cookieHelper.setAuthCookie(
                response,
                "refreshToken",
                refreshToken,
                259200 // 30 days
        );

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
    public AuthResponse login(
            AuthRequest request,
            HttpServletResponse response
    ) {
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

        // ========================
        // GENERATE TOKENS
        // ========================
        String accessToken = jwtService.generateAccessToken(
                String.valueOf(user.getId()),
                user.getEmail(),
                user.getUsername()
        );

        String refreshToken = jwtService.generateRefreshToken(
                String.valueOf(user.getId())
        );

        // ========================
        // SAVE TOKENS IN COOKIE
        // ========================
        cookieHelper.setAuthCookie(
                response,
                "accessToken",
                accessToken,
                300 // 5 minutes
        );

        cookieHelper.setAuthCookie(
                response,
                "refreshToken",
                refreshToken,
                259200 // 30 days
        );

        // ========================
        // INCLUDE ACCESS TOKEN IN THE RESPONSE
        // ========================
        return new AuthResponse(
                "Login successfully",
                userResponse,
                accessToken
        );
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
