package com.rental.PropertyRentalApi.Service;

import java.util.List;

import com.rental.PropertyRentalApi.Entity.UserEntity;

public interface UserService {
    List<UserEntity> getAll();
    UserEntity getById(Long id);
    UserEntity update(Long id, UserEntity request);
    UserEntity create(UserEntity request);
    void delete(Long id);
}
