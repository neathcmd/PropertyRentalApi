package com.rental.PropertyRentalApi.DTO.request;

import lombok.Data;

@Data
public class VerifyOTPRequest {
    private String email;
    private String otpCode;
}
