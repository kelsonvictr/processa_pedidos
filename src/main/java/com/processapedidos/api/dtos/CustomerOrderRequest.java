package com.processapedidos.api.dtos;

import com.processapedidos.api.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerOrderRequest {
    private UUID memberId;
    private Set<OrderItem> orderItemSet;

}
