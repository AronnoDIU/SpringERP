package com.springerp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Company.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDTO {
    private Long id;
    private String companyName;
    private String companyCode;
    private String registrationNumber;
    private String taxId;
    private String vatNumber;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String phone;
    private String email;
    private String website;
    private String currency;
    private String logoUrl;
    private Long parentCompanyId;
    private String status;
    private String subscriptionTier;
}

