package com.rental.PropertyRentalApi.Service.Impl;

import com.rental.PropertyRentalApi.Entity.UserEntity;
import com.rental.PropertyRentalApi.Service.UserService;
import com.rental.PropertyRentalApi.Repository.UserRepository;
import com.rental.PropertyRentalApi.Utils.HelperFunction;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.notFound;

@Service
@RequiredArgsConstructor
public class UserSreviceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HelperFunction helperFunction;

    @Override
    public List<UserEntity> getAll() {
        List<UserEntity> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw notFound("User not found.");
        }

        return users;

    }

    @Override
    public UserEntity getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> notFound("User not found."));
    }

    @Override
    public UserEntity update(Long id, UserEntity request) {
        UserEntity findUserAndUpdate = getById(id);
        helperFunction.validateUpdate(id, request);

        findUserAndUpdate.setFullname(request.getFullname());
        findUserAndUpdate.setUsername(request.getUsername());
        findUserAndUpdate.setEmail(request.getEmail());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            findUserAndUpdate.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return userRepository.save(findUserAndUpdate);
    }

    @Override
    public UserEntity create(UserEntity request) {
        helperFunction.validateCreate(request);
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(request);
    }

    @Override
    public void delete(Long id) {
        UserEntity findUserAndDelete = getById(id);
        userRepository.delete(findUserAndDelete);
    }
}
