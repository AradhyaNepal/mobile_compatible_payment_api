package com.a2.mobile_compatible_payment_api.repository;

import com.a2.mobile_compatible_payment_api.entity.MyUsers;
import com.a2.mobile_compatible_payment_api.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<MyUsers, Integer> {
}

