package com.rental.PropertyRentalApi.Service;

import com.rental.PropertyRentalApi.DTO.request.RegisterRequest;
import com.rental.PropertyRentalApi.DTO.response.RegisterResponse;
import org.springframework.stereotype.Service;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
}
