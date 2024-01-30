package com.processapedidos.api.repository;

import com.processapedidos.api.model.Commission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommissionRepository extends JpaRepository<Commission, UUID> {
}
