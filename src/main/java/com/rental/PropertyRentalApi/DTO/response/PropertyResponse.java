package com.rental.PropertyRentalApi.DTO.response;

import com.rental.PropertyRentalApi.Entity.PropertyEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyResponse {
    private Long id;
    private String title;
    private String description;
    private String address;
    private BigDecimal price;
    private BigDecimal electricCost;
    private BigDecimal waterCost;

    private UserResponse createdBy;

    public PropertyResponse(PropertyEntity propertyEntity) {
        this.id = propertyEntity.getId();
        this.title = propertyEntity.getTitle();
        this.description = propertyEntity.getDescription();
        this.address = propertyEntity.getAddress();
        this.price = propertyEntity.getPrice();
        this.electricCost = propertyEntity.getElectricityCost();
        this.waterCost = propertyEntity.getWaterCost();
    }
}
