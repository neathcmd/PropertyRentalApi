package com.rental.PropertyRentalApi.DTO.response;

import com.rental.PropertyRentalApi.Entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String fullname;
    private String username;
    private String email;
//    private String role;

    public UserResponse(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.fullname = userEntity.getFullname();
        this.username = userEntity.getUsername();
        this.email = userEntity.getEmail();
//        this.role = userEntity.getRole().getName();
    }

}

