package com.rental.PropertyRentalApi.DTO.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String email;
    private String otpCode;
    private String newPassword;
}
