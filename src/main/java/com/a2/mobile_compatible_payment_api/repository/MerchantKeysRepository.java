package com.a2.mobile_compatible_payment_api.repository;
import com.a2.mobile_compatible_payment_api.entity.MerchantKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MerchantKeysRepository extends JpaRepository<MerchantKeys, UUID> {
    @Query("SELECT m FROM MerchantKeys m WHERE m.isDefault = true")
    Optional<MerchantKeys> getDefaultMerchant();
}

