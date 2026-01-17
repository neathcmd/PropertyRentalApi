package com.rental.PropertyRentalApi.DTO.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse<T> {

    private Integer status;
    private String message;
    private T data;

    // Optional convenience constructors
    public ApiResponse(String message) {
        this.message = message;
    }

    public ApiResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiResponse(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
