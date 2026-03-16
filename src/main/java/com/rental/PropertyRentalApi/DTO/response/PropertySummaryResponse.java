package com.rental.PropertyRentalApi.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertySummaryResponse {
    private Long id;
    private String title;
    private String address;
    private BigDecimal price;
    private Boolean available;
    private CategoryResponse category;
    private List<String> images;
}