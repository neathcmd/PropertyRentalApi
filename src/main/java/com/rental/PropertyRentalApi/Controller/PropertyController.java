package com.rental.PropertyRentalApi.Controller;

import com.rental.PropertyRentalApi.Entity.PropertyEntity;
import com.rental.PropertyRentalApi.Service.JwtService;
import com.rental.PropertyRentalApi.Service.PropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;
    private final JwtService jwtService;

    // Create Property
    @PostMapping("/")
    public ResponseEntity<PropertyEntity> createProperty(@RequestBody PropertyEntity propertyRequest) {
        PropertyEntity createdProperty = propertyService.create(propertyRequest);
        return new ResponseEntity<>(createdProperty, HttpStatus.CREATED);
    }
}

