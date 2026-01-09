
package com.rental.PropertyRentalApi.Service;

import com.rental.PropertyRentalApi.Entity.PropertyEntity;

import java.util.List;

public interface PropertyService {

    PropertyEntity create(PropertyEntity property);

    List<PropertyEntity> getAll();

    PropertyEntity getById(Long id);

    PropertyEntity update(Long id, PropertyEntity property);

    void delete(Long id);
}