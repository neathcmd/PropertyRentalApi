package com.rental.PropertyRentalApi.Controller;

import com.rental.PropertyRentalApi.DTO.response.ApiResponse;
import com.rental.PropertyRentalApi.DTO.response.UserResponse;
import com.rental.PropertyRentalApi.Entity.UserEntity;
import com.rental.PropertyRentalApi.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.notFound;

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

    @GetMapping("/{id}")
    public ApiResponse<UserEntity> getUserById(@PathVariable Long id) {
        return new ApiResponse<>(
                200,
                "Get user successfully.",
                userService.getById(id)
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<UserEntity> updateUser(
            @PathVariable Long id,
            @RequestBody UserEntity updatedUser
    ) {
        return new ApiResponse<>(
                200,
                "User update successfully.",
                userService.update(id, updatedUser)
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return new ApiResponse<>(
                "User delete successfully."
        );
    }
}
