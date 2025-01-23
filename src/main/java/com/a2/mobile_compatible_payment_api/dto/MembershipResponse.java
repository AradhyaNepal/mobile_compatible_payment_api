package com.a2.mobile_compatible_payment_api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MembershipResponse {
    private String membershipCode;
    private String membershipName;
    private String membershipAmount;
    private String membershipBenefits;
}
