package org.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "pg_listings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PgListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // e.g. "Sri Balaji Luxury Coliving"

    @Column(nullable = false)
    private String address; // e.g. "#42, 14th Main Road, near BDA Complex"

    // 1. RELATIONSHIP: Which Bangalore area does this PG belong to?
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    // 2. RELATIONSHIP: Which registered PG Owner owns this property?
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private User owner;

    // Bangalore-specific living criteria (Using our Enums!)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GenderType genderType; // MALE, FEMALE, COLIVING

    @Enumerated(EnumType.STRING)
    private FoodType foodType; // NORTH_INDIAN, SOUTH_INDIAN, BOTH, NONE

    private boolean foodIncluded; // true/false

    // Pricing & Transparency
    @Column(nullable = false)
    private BigDecimal startingPrice; // e.g. 8500.00 (min rent for quick budget filtering)

    private BigDecimal depositAmount; // e.g. 10000.00
    private String noticePeriod;      // e.g. "15 Days" / "30 Days"
    private String gateClosingTime;   // e.g. "10:30 PM" / "24/7 Access"

    // Direct Lead Contacts (Zero Brokerage)
    @Column(nullable = false)
    private String contactPhone;

    private String whatsappNumber; // For the 1-click "Chat on WhatsApp" button

    // Status Flags
    private boolean isVerified;  // "Physically Verified" badge given by Admin
    private boolean isAvailable; // Owner can toggle if full
}