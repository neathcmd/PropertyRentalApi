package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.Entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyRepository extends JpaRepository<PropertyEntity, Long> {
    boolean existsByTitle(String title);
    List<PropertyEntity> findByDeletedFalse();
}

