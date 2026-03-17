package com.rental.PropertyRentalApi.Service;

import com.rental.PropertyRentalApi.DTO.request.AuthRequest;
import com.rental.PropertyRentalApi.DTO.request.RegisterRequest;
import com.rental.PropertyRentalApi.DTO.response.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {

    RegisterResponse register(
            RegisterRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse response,
            MultipartFile profileImage
    );

    AuthResponse login(
            AuthRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse response
    );

    ApiResponse<Object> logout(
            HttpServletRequest request,
            HttpServletResponse response
    );
}