package com.rental.PropertyRentalApi.Service.Impl;

import com.rental.PropertyRentalApi.DTO.request.AuthRequest;
import com.rental.PropertyRentalApi.DTO.request.RegisterRequest;
import com.rental.PropertyRentalApi.DTO.response.*;
import com.rental.PropertyRentalApi.Entity.Roles;
import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Mapper.UserMapper;
import com.rental.PropertyRentalApi.Repository.RoleRepository;
import com.rental.PropertyRentalApi.Repository.UserRepository;
import com.rental.PropertyRentalApi.Service.AuthService;
import com.rental.PropertyRentalApi.Service.Jwt.JwtService;
import com.rental.PropertyRentalApi.Service.UploadService;
import com.rental.PropertyRentalApi.Utils.CookieHelper;
import com.rental.PropertyRentalApi.Utils.UserValidatorUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.*;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class AuthServiceImpl implements AuthService {

    private static final int COOKIE_MAX_AGE = 60 * 24 * 60 * 60; // 60 days in seconds

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CookieHelper cookieHelper;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final UserValidatorUtil userValidator;
    private final UploadService uploadService;

    @Override
    @Transactional
    public RegisterResponse register(
            RegisterRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse response,
            MultipartFile profileImage
    ) {
        // ========================
        // VALIDATE UNIQUENESS
        // ========================
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw badRequest("Username already exists");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw badRequest("Email already exists");
        }
        userValidator.validateEmailFormat(request.getEmail());

        // ========================
        // BUILD USER ENTITY
        // ========================
        Users user = userMapper.toUserEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // ========================
        // ASSIGN ROLES
        // ========================
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            List<Roles> roles = roleRepository.findAllById(request.getRoles());
            if (roles.size() != request.getRoles().size()) {
                throw badRequest("Some roles not found.");
            }
            user.setRoles(new HashSet<>(roles));
        } else {
            Roles defaultRole = roleRepository.findByName("user")
                    .orElseThrow(() -> notFound("Default role not found."));
            user.setRoles(new HashSet<>(Set.of(defaultRole)));
        }

        // ========================
        // SAVE USER
        // ========================
        Users savedUser = userRepository.save(user);

        // ========================
        // UPLOAD PROFILE IMAGE
        // ========================
        if (profileImage != null && !profileImage.isEmpty()) {
            uploadService.uploadUserProfile(savedUser.getId(), profileImage);
        }

        // ========================
        // GENERATE TOKEN & SET COOKIE
        // ========================
        String token = generateTokenAndSetCookie(savedUser, response);

        return new RegisterResponse(
                201,
                true,
                "Register successfully.",
                userMapper.toUserResponse(savedUser)
        );
    }

    @Override
    public AuthResponse login(
            AuthRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse response
    ) {
        // ========================
        // FIND & VALIDATE USER
        // ========================
        Users user = userRepository.findByEmail(request.getEmail_or_username())
                .or(() -> userRepository.findByUsername(request.getEmail_or_username()))
                .orElseThrow(() -> notFound("User not found."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw unauthorized("Invalid password");
        }

        // ========================
        // GENERATE TOKEN & SET COOKIE
        // ========================
        String token = generateTokenAndSetCookie(user, response);

        return new AuthResponse(
                200,
                true,
                "Login successfully.",
                token,
                userMapper.toUserResponse(user)
        );
    }

    @Override
    public ApiResponse<Object> logout(HttpServletRequest request, HttpServletResponse response) {
        cookieHelper.clearAuthCookie(response, "token");

        return new ApiResponse<>(
                200,
                true,
                "User logout successfully."
        );
    }

    // ========================
    // PRIVATE HELPER
    // ========================
    private String generateTokenAndSetCookie(Users user, HttpServletResponse response) {
        List<String> roles = user.getRoles()
                .stream()
                .map(Roles::getName)
                .toList();

        String token = jwtService.generateToken(
                String.valueOf(user.getId()),
                user.getEmail(),
                user.getUsername(),
                roles
        );

        cookieHelper.setAuthCookie(response, "token", token, COOKIE_MAX_AGE);

        return token;
    }
}