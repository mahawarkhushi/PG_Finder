package org.example.dto;

import org.example.model.FoodType;
import org.example.model.GenderType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreatePgRequest {
    private String name;
    private String address;
    private Long areaId; // The ID of the Bangalore area it belongs to
    private GenderType genderType;
    private FoodType foodType;
    private boolean foodIncluded;
    private BigDecimal startingPrice;
    private BigDecimal depositAmount;
    private String noticePeriod;
    private String gateClosingTime;
    private String contactPhone;
    private String whatsappNumber;
}