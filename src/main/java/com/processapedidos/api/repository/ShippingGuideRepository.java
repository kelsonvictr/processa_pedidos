package com.processapedidos.api.repository;

import com.processapedidos.api.model.ShippingGuide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingGuideRepository extends JpaRepository<ShippingGuide, Long> {
}
