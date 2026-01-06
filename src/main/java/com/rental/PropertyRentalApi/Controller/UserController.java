package com.rental.PropertyRentalApi.Controller;

import com.rental.PropertyRentalApi.DTO.response.ApiResponse;
import com.rental.PropertyRentalApi.DTO.response.UserResponse;
import com.rental.PropertyRentalApi.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUser() {
        List<UserResponse> users = userService.getAll()
                .stream()
                .map(UserResponse::new)
                .toList();

        return new ApiResponse<>(
                200,
                "Get user successfully.",
                users
        );

    }
}
