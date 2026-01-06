package com.rental.PropertyRentalApi.Scripts;

import com.rental.PropertyRentalApi.Entity.PropertyEntity;
import com.rental.PropertyRentalApi.Repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(3) // Make sure it runs after other seeders if needed
public class PropertySeeder implements CommandLineRunner {

    private final PropertyRepository propertyRepository;

    @Override
    public void run(String... args) {
        log.info("Seeding properties...");

        // Seed properties
        seedProperty("Cozy Studio Apartment", "Small but comfortable studio near campus",
                "Street 123, Phnom Penh", new BigDecimal("120"), new BigDecimal("10"), new BigDecimal("5"));

        seedProperty("Spacious 2 Bedroom Flat", "Perfect for roommates or family",
                "Street 456, Phnom Penh", new BigDecimal("250"), new BigDecimal("20"), new BigDecimal("10"));

        seedProperty("Modern Condo", "Luxury condo with pool and gym",
                "Street 789, Phnom Penh", new BigDecimal("400"), new BigDecimal("30"), new BigDecimal("15"));

        log.info("Property seeding completed.");
    }

    /**
     * Seed a property only if it does not already exist
     */
    private void seedProperty(String title, String description, String address,
                              BigDecimal price, BigDecimal electricityCost, BigDecimal waterCost) {

        // Check if property already exists
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
        property.setCreatedAt(LocalDateTime.now());
        property.setUpdatedAt(LocalDateTime.now());
        property.setIsDeleted(false); // works if your setter is correct

        propertyRepository.save(property);
        log.info("Seeded property '{}'", title);
    }
}
