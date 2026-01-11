package com.rental.PropertyRentalApi.Mapper;

import com.rental.PropertyRentalApi.DTO.request.*;
import com.rental.PropertyRentalApi.DTO.response.PropertyResponse;
import com.rental.PropertyRentalApi.DTO.response.UserResponse;
import com.rental.PropertyRentalApi.Entity.PropertyEntity;
import com.rental.PropertyRentalApi.Entity.RoleEntity;
import com.rental.PropertyRentalApi.Entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface MapperFunction {

    // ==============
    // PROPERTY REQUEST MAPPINGS
    // ==============
    PropertyEntity toPropertyEntity(PropertyCreateRequest request);

    void updatePropertyEntity(
            PropertyUpdateRequest request,
            @MappingTarget PropertyEntity entity
    );

    // ==============
    // PROPERTY RESPONSE MAPPINGS
    // ==============
    @Mapping(source = "createdBy", target = "createdBy")
    PropertyResponse toPropertyResponse(PropertyEntity property);

    // ==============
    // REGISTER REQUEST MAPPINGS
    // ==============
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserEntity toUserEntity(RegisterRequest request);


    // ==============
    // USER REQUEST MAPPINGS
    // ==============
    UserEntity toUserEntity(UserCreateRequest request);

    void updateUserEntity(
            UserUpdateRequest request,
            @MappingTarget UserEntity entity
    );

    // ==============
    // USER RESPONSE MAPPINGS
    // ==============
    @Mapping(target = "roles", source = "roles")
    UserResponse toUserResponse(UserEntity user);

    // ==============
    // ROLES MAPPINGS
    // ==============
    default List<String> mapRoles(Set<RoleEntity> roles) {
        if (roles == null) {
            return List.of();
        }
        return roles.stream()
                .map(RoleEntity::getName)
                .toList();
    }
}