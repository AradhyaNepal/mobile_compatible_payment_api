package com.a2.mobile_compatible_payment_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class MyUsers {
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private String fullName;
}
