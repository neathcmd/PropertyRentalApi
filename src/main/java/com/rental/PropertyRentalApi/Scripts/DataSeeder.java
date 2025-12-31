//package com.rental.PropertyRentalApi.Scripts;
//
//import com.rental.PropertyRentalApi.Entity.UserEntity;
//import com.rental.PropertyRentalApi.Repository.UserRepository;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class DataSeeder {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
////    private final UserEntity User;
//
//    @Override
//    public void run(String... args) {
//        seedAdmin();
//    }
//
//    private void seedAdmin() {
//        String adminEmail = "adminwmad12@gmail.com";
//
//        if (userRepository.existsByEmail(adminEmail)) {
//            return;
//        }
//
//        UserEntity admin = UserEntity.builder()
//                .username("admin")
//                .email(adminEmail)
//                .password(passwordEncoder.encode("Admin@123"))
////                .role(Role.ADMIN)
//                .enabled(true)
//                .build();
//
//        userRepository.save(admin);
//
//        System.out.println("✅ Admin user seeded successfully");
//    }
//}
