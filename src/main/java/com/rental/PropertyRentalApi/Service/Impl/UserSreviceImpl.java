package com.rental.PropertyRentalApi.Service.Impl;

import com.rental.PropertyRentalApi.Entity.UserEntity;
import com.rental.PropertyRentalApi.Service.UserService;
import com.rental.PropertyRentalApi.Repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSreviceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }
}
