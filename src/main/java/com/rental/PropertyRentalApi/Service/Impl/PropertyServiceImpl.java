package com.rental.PropertyRentalApi.Service.Impl;

import com.rental.PropertyRentalApi.Entity.PropertyEntity;
import com.rental.PropertyRentalApi.Entity.UserEntity;
import com.rental.PropertyRentalApi.Repository.PropertyRepository;
import com.rental.PropertyRentalApi.Service.JwtService;
import com.rental.PropertyRentalApi.Service.PropertyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final JwtService jwtService;

    // ================= CREATE =================
    @Override
    @Transactional
    public PropertyEntity create(PropertyEntity createProperty) {

        UserEntity currentUser = jwtService.getCurrentUser();

        createProperty.setId(null);                // prevent overwrite
        createProperty.setCreatedBy(currentUser);  // owner
        createProperty.setIsDeleted(false);
        createProperty.setCreatedAt(LocalDateTime.now());

        return propertyRepository.save(createProperty);
    }

    // ================= READ ALL =================
    @Override
    public List<PropertyEntity> getAll() {
        return propertyRepository.findByIsDeletedFalse();
    }

    // ================= READ BY ID =================
    @Override
    public PropertyEntity getById(Long id) {
        return (PropertyEntity) propertyRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Property not found with id: " + id));
    }
    // ================= UPDATE =================
    @Override
    @Transactional
    public PropertyEntity update(Long id, PropertyEntity request) {

        UserEntity currentUser = jwtService.getCurrentUser();

        PropertyEntity property = getById(id);

        // 🔒 Only owner can update
        if (!property.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to update this property");
        }

        // ✏️ Update allowed fields ONLY
        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setPrice(request.getPrice());
        property.setAddress(request.getAddress());
        property.setUpdatedAt(LocalDateTime.now());

        return propertyRepository.save(property);
    }

    // ================= DELETE (SOFT) =================
    @Override
    @Transactional
    public void delete(Long id) {

        UserEntity currentUser = jwtService.getCurrentUser();

        PropertyEntity property = getById(id);

        // 🔒 Only owner can delete
        if (!property.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to delete this property");
        }

        property.setIsDeleted(true);
        property.setUpdatedAt(LocalDateTime.now());

        propertyRepository.save(property);
    }
}
