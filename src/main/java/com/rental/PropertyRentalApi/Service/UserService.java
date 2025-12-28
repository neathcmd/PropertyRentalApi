package com.rental.PropertyRentalApi.Service;

import java.util.List;

import com.rental.PropertyRentalApi.Entity.UserEntity;

public interface UserService {
    List<UserEntity> getAll();
}
