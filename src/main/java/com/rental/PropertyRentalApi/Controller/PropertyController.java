package com.rental.PropertyRentalApi.Controller;

import com.rental.PropertyRentalApi.Entity.PropertyEntity;
import com.rental.PropertyRentalApi.Service.JwtService;
import com.rental.PropertyRentalApi.Service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;
    private final JwtService jwtService;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<PropertyEntity> createProperty(
            @RequestBody PropertyEntity propertyRequest
    ) {
        PropertyEntity createdProperty = propertyService.create(propertyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProperty);
    }

    // ================= READ ALL =================
    @GetMapping
    public ResponseEntity<List<PropertyEntity>> getAllProperties() {
        return ResponseEntity.ok(propertyService.getAll());
    }

    // ================= READ BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<PropertyEntity> getPropertyById(@PathVariable Long id) {
        PropertyEntity property = propertyService.getById(id);
        return ResponseEntity.ok(property);
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<PropertyEntity> updateProperty(
            @PathVariable Long id,
            @RequestBody PropertyEntity propertyRequest
    ) {
        PropertyEntity updatedProperty = propertyService.update(id, propertyRequest);
        return ResponseEntity.ok(updatedProperty);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        propertyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
