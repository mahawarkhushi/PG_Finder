package org.example.controller;

import org.example.dto.CreatePgRequest;
import org.example.model.Area;
import org.example.model.GenderType;
import org.example.model.PgListing;
import org.example.repository.AreaRepository;
import org.example.repository.PgListingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pgs")
@CrossOrigin(origins = "*")
public class PgListingController {

    private final PgListingRepository pgListingRepository;
    private final AreaRepository areaRepository;

    public PgListingController(PgListingRepository pgListingRepository, AreaRepository areaRepository) {
        this.pgListingRepository = pgListingRepository;
        this.areaRepository = areaRepository;
    }

    // 1. Add a new PG: POST http://localhost:8080/api/v1/pgs
    @PostMapping
    public ResponseEntity<?> createPg(@RequestBody CreatePgRequest request) {
        // Find the Area in PostgreSQL
        Area area = areaRepository.findById(request.getAreaId())
                .orElse(null);

        if (area == null) {
            return ResponseEntity.badRequest().body("Error: Area with ID " + request.getAreaId() + " not found!");
        }

        // Build and save the new PG
        PgListing pg = PgListing.builder()
                .name(request.getName())
                .address(request.getAddress())
                .area(area)
                .genderType(request.getGenderType())
                .foodType(request.getFoodType())
                .foodIncluded(request.isFoodIncluded())
                .startingPrice(request.getStartingPrice())
                .depositAmount(request.getDepositAmount())
                .noticePeriod(request.getNoticePeriod())
                .gateClosingTime(request.getGateClosingTime())
                .contactPhone(request.getContactPhone())
                .whatsappNumber(request.getWhatsappNumber())
                .isAvailable(true)
                .isVerified(false)
                .build();

        PgListing savedPg = pgListingRepository.save(pg);
        return ResponseEntity.status(201).body(savedPg);
    }

    // 2. Fetch PGs with optional filters: GET http://localhost:8080/api/v1/pgs?areaId=1&gender=MALE&maxPrice=12000
    @GetMapping
    public List<PgListing> getPgs(
            @RequestParam(required = false) Long areaId,
            @RequestParam(required = false) GenderType gender,
            @RequestParam(required = false) BigDecimal maxPrice
    ) {
        if (areaId != null && gender != null) {
            return pgListingRepository.findByAreaIdAndGenderType(areaId, gender);
        }
        if (areaId != null && maxPrice != null) {
            return pgListingRepository.findByAreaIdAndStartingPriceLessThanEqual(areaId, maxPrice);
        }
        if (areaId != null) {
            return pgListingRepository.findByAreaId(areaId);
        }
        return pgListingRepository.findAll();
    }

    // 3. Fetch single PG details: GET http://localhost:8080/api/v1/pgs/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PgListing> getPgById(@PathVariable Long id) {
        return pgListingRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}