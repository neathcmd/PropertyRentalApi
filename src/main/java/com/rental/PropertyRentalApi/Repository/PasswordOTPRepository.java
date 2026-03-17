package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.Entity.PasswordOTP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordOTPRepository extends JpaRepository<PasswordOTP, Long> {
    Optional<PasswordOTP> findTopByEmailOrderByCreatedAtDesc(String email);
}
