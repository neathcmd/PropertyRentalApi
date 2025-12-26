package com.rental.PropertyRentalApi.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rental.PropertyRentalApi.Repository.UserRepository;
import com.rental.PropertyRentalApi.Entity.UserEntity;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
}