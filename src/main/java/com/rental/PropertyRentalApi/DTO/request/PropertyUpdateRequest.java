package com.rental.PropertyRentalApi.DTO.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PropertyUpdateRequest {
    private String title;
    private String description;
    private String address;
    private BigDecimal price;
    private BigDecimal electricityCost;
    private BigDecimal waterCost;
}
