package com.rental.PropertyRentalApi.DTO.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String fullname;
    private String username;
    private String email;
    private String password;
    private String phone;
}
