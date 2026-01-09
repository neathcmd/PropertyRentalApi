
package com.rental.PropertyRentalApi.Service;

import com.rental.PropertyRentalApi.Entity.PropertyEntity;

import java.util.List;

public interface PropertyService {

    PropertyEntity create(PropertyEntity createProperty);

    // Optional: add list method
    List<PropertyEntity> getAllPosts();
    List<PropertyEntity> getAll();
    PropertyEntity getById(Long id);
    PropertyEntity update(Long id, PropertyEntity post);
    void delete(Long id);
   
}