package com.sparta.java_02.domain.purchase.repository;

import com.sparta.java_02.domain.purchase.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

}
