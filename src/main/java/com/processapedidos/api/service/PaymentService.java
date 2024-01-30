package com.processapedidos.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.processapedidos.api.dtos.PaymentRequest;
import com.processapedidos.api.model.Payment;

public interface PaymentService {
    void create(PaymentRequest paymentRequest) throws JsonProcessingException;
    void processReceivedPayments(Payment payment);

}
