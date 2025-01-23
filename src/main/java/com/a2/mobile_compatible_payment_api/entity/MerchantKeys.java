package com.a2.mobile_compatible_payment_api.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MerchantKeys")
public class MerchantKeys {
    @Id
    @GeneratedValue
    private int id;


    @Column(unique = true, nullable = false)
    private String merchantCode;

    @NotNull
    private String merchantName;


    @NotNull
    private String merchantEmail;


    @NotNull
    private String esewaSecretKey;

    @NotNull
    private String esewaClientSecret;


    @NotNull
    private String esewaClientId;

    @NotNull
    private String websiteUrl;

    @NotNull
    private String khaltiSecret;

    @NotNull
    private String khaltiPublic;

    @NotNull
    private String returnUrl;

    @NotNull
    private Boolean isDefault;


}
