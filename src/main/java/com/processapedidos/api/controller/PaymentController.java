package com.processapedidos.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.processapedidos.api.dtos.PaymentRequest;
import com.processapedidos.api.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody PaymentRequest paymentRequest) throws JsonProcessingException {
        paymentService.create(paymentRequest);
    }

}
