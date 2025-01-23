package com.a2.mobile_compatible_payment_api.repository;

import com.a2.mobile_compatible_payment_api.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MembershipRepository extends JpaRepository<Membership, Integer> {
    @Query("SELECT m FROM Membership m WHERE m.membershipCode = :membershipCode")
    Optional<Membership> getByCode(@Param("membershipCode") String membershipCode);

}

