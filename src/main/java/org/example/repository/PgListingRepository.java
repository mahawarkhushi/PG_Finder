package org.example.repository;

import org.example.model.GenderType;
import org.example.model.PgListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PgListingRepository extends JpaRepository<PgListing, Long> {

    // 1. Fetch all PGs in a specific Bangalore area
    List<PgListing> findByAreaId(Long areaId);

    // 2. Fetch all PGs created by a specific Owner (for their dashboard)
    List<PgListing> findByOwnerId(Long ownerId);

    // 3. Filter by Area AND Gender (e.g. Ladies PGs in HSR Layout)
    List<PgListing> findByAreaIdAndGenderType(Long areaId, GenderType genderType);

    // 4. Filter by Area AND Maximum Budget (e.g. Under ₹12,000)
    List<PgListing> findByAreaIdAndStartingPriceLessThanEqual(Long areaId, BigDecimal maxPrice);
}