package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.Entity.PropertyEntity;
import com.rental.PropertyRentalApi.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity, Long> {
    boolean existsByTitle(String title);
    List<PropertyEntity> findAllByCreatedBy(UserEntity user);
}
