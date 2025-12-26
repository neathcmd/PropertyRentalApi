package com.rental.PropertyRentalApi.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
	private String username;
    private String fullname;
    private String email;
    private String token; 
    
}

