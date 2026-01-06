package com.rental.PropertyRentalApi.Scripts;

import com.rental.PropertyRentalApi.Entity.PropertyEntity;
import com.rental.PropertyRentalApi.Entity.UserEntity;
import com.rental.PropertyRentalApi.Repository.PropertyRepository;
import com.rental.PropertyRentalApi.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(3) // Make sure it runs after Role/User seeding
public class PropertySeeder implements CommandLineRunner {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        log.info("Seeding properties...");

        // Fetch some users to assign as 'agents'
        List<UserEntity> users = userRepository.findAll();
        if (users.isEmpty()) {
            log.warn("No users found! Please seed users first.");
            return;
        }

        // Example: assign first user as agent for seeding
        UserEntity agent = users.get(0);

        seedProperty("Cozy Studio Apartment", "Small but comfortable studio near campus",
                "Street 123, Phnom Penh", new BigDecimal("120"), new BigDecimal("10"), new BigDecimal("5"), agent);

        seedProperty("Spacious 2 Bedroom Flat", "Perfect for roommates or family",
                "Street 456, Phnom Penh", new BigDecimal("250"), new BigDecimal("20"), new BigDecimal("10"), agent);

        seedProperty("Modern Condo", "Luxury condo with pool and gym",
                "Street 789, Phnom Penh", new BigDecimal("400"), new BigDecimal("30"), new BigDecimal("15"), agent);

        log.info("Property seeding completed.");
    }

    private void seedProperty(String title, String description, String address,
                              BigDecimal price, BigDecimal electricityCost, BigDecimal waterCost,
                              UserEntity agent) {

        if (propertyRepository.existsByTitle(title)) {
            log.info("Property '{}' already exists. Skipping.", title);
            return;
        }

        PropertyEntity property = new PropertyEntity();

        property.setTitle(title);
        property.setDescription(description);
        property.setAddress(address);
        property.setPrice(price);
        property.setElectricityCost(electricityCost);
        property.setWaterCost(waterCost);
        property.setCreatedBy(agent);
        property.setCreatedAt(LocalDateTime.now());
        property.setUpdatedAt(LocalDateTime.now());
        property.setIsDeleted(false);

        propertyRepository.save(property);
        log.info("Seeded property '{}'", title);
    }
}
