package com.rental.PropertyRentalApi.Utils;

import com.rental.PropertyRentalApi.Entity.UserEntity;
import com.rental.PropertyRentalApi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.badRequest;

@Component
public class HelperFunction {

    @Autowired
    private UserRepository userRepository;

    // ================
    // EMAIL FORMAT VALIDATOR
    // ================
    public void validateEmailFormat(String email) {
        if (email == null || !email.contains("@") || !email.endsWith(".com")) {
            throw badRequest("Email must contain '@' and end with '.com'");
        }
    }

    // ================
    // CREATE FIELDS VALIDATOR
    // ================
    public void validateCreate(UserEntity request) {
        validateEmailFormat(request.getEmail());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw badRequest("Email is already in use.");
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw badRequest("Username is already in use.");
        }
    }

    // ================
    // UPDATE FIELDS VALIDATOR
    // ================
    public void validateUpdate(Long id, UserEntity request) {
        validateEmailFormat(request.getEmail());

        userRepository.findByEmail(request.getEmail()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw badRequest("Email is already used by another user.");
            }
        });

        userRepository.findByUsername(request.getUsername()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw badRequest("Username is already used by another user.");
            }
        });
    }
}
