package com.rental.PropertyRentalApi.Service;


import com.rental.PropertyRentalApi.DTO.request.AuthRequest;
import com.rental.PropertyRentalApi.DTO.request.RegisterRequest;
import com.rental.PropertyRentalApi.DTO.response.ApiResponse;
import com.rental.PropertyRentalApi.DTO.response.AuthResponse;
import com.rental.PropertyRentalApi.DTO.response.RegisterResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public interface AuthService {
    RegisterResponse register(RegisterRequest request,
                              HttpServletResponse response);
    AuthResponse login(AuthRequest request,
                       HttpServletResponse response);

    ApiResponse<Object> logout(HttpServletRequest request,
                               HttpServletResponse response);
}
