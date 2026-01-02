package com.rental.PropertyRentalApi.DTO.response;

import com.rental.PropertyRentalApi.Entity.RoleEntity;
import com.rental.PropertyRentalApi.Entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String fullname;
    private String username;
    private String email;
    private List<String> roles;

    public UserResponse(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.fullname = userEntity.getFullname();
        this.username = userEntity.getUsername();
        this.email = userEntity.getEmail();
        this.roles = userEntity.getRoles()
                .stream()
                .map(RoleEntity::getName) // extract name
                .collect(Collectors.toList());
    }
}

