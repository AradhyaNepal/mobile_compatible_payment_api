package com.a2.mobile_compatible_payment_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MyUsers")
public class MyUsers {
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private String fullName;

    private boolean isPremium;
}
