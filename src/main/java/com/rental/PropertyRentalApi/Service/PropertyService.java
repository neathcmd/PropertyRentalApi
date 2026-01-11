package com.rental.PropertyRentalApi.Service;

import com.rental.PropertyRentalApi.DTO.request.PropertyCreateRequest;
import com.rental.PropertyRentalApi.DTO.request.PropertyUpdateRequest;
import com.rental.PropertyRentalApi.DTO.response.PropertyResponse;
import com.rental.PropertyRentalApi.DTO.response.PaginatedResponse;

import java.util.List;

public interface PropertyService {
    PaginatedResponse<PropertyResponse> getAll(int page, int size);

//    List<PropertyResponse> getAll();
    PropertyResponse getById(Long id);
    PropertyResponse create(PropertyCreateRequest request);
    PropertyResponse update(Long id, PropertyUpdateRequest request);
    void delete(Long id);

    List<PropertyResponse> getPropertiesByCurrentUser();
}
