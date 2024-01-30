package com.processapedidos.api.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentRequest {
    private UUID memberId;
    private UUID customerOrderId;

}
