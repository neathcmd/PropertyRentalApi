package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.Entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    // Add this:
    Optional<UserEntity> findByEmail(String email);
}
