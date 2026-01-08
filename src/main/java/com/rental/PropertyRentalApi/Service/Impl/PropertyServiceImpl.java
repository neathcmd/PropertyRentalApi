package com.rental.PropertyRentalApi.Service.Impl;

import com.rental.PropertyRentalApi.Entity.PropertyEntity;
import com.rental.PropertyRentalApi.Entity.UserEntity;
import com.rental.PropertyRentalApi.Repository.PropertyRepository;
//import com.rental.PropertyRentalApi.Repository.UserRepository;
import com.rental.PropertyRentalApi.Service.JwtService;
import com.rental.PropertyRentalApi.Service.PropertyService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final JwtService jwtService;
//    private final UserRepository userRepository;

    @Override
    @Transactional
    public PropertyEntity create(PropertyEntity createProperty) {
        // 1. Get authenticated user
        UserEntity currentUser = jwtService.getCurrentUser();

        // 2. Protect fields that client must NOT control
        createProperty.setId(null);          // prevent overwrite
        createProperty.setCreatedBy(currentUser);
        createProperty.setIsDeleted(false);  // ensure default state

        // 3. Save
        return propertyRepository.save(createProperty);
    }

    @Override
    public List<PropertyEntity> getAllPosts() {
        return List.of();
    }

    @Override
    public List<PropertyEntity> getAll() {
        return List.of();
    }

    @Override
    public PropertyEntity getById(Long id) {
        return null;
    }

    @Override
    public PropertyEntity update(Long id, PropertyEntity post) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}

