package com.processapedidos.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.processapedidos.api.model.Payment;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentMessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    public void sendPayment(Payment payment) throws JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());
        String paymentJson = objectMapper.writeValueAsString(payment);
        rabbitTemplate.convertAndSend("payments", paymentJson);
    }
}
