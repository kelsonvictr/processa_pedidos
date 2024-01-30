package com.processapedidos.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.processapedidos.api.model.Payment;
import com.processapedidos.api.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentMessageReceiver {

    private static final Logger logger = LoggerFactory.getLogger(PaymentMessageReceiver.class);

    @Autowired
    PaymentService paymentService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "payments")
    public void receivePayment(String paymentJson) {
        try {
            objectMapper.registerModule(new JavaTimeModule());
            Payment payment = objectMapper.readValue(paymentJson, Payment.class);
            logger.info("Received payment: {}", payment);
            paymentService.processReceivedPayments(payment);
        } catch (JsonProcessingException e) {
            logger.error("Error processing JSON", e);
        }
    }
}