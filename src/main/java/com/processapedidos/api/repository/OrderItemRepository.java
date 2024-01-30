package com.processapedidos.api.repository;

import com.processapedidos.api.model.OrderItem;
import com.processapedidos.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    @Query("SELECT oi.product FROM OrderItem oi WHERE oi.customerOrder.id = :customerOrderId")
    List<Product> findProductsByCustomerOrderId(UUID customerOrderId);
}
