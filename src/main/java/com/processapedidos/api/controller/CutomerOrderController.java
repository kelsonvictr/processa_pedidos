package com.processapedidos.api.controller;

import com.processapedidos.api.dtos.CustomerOrderRequest;
import com.processapedidos.api.service.CustomerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer-order")
public class CutomerOrderController {

    @Autowired
    CustomerOrderService customerOrderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody CustomerOrderRequest customerOrderRequest) {
        customerOrderService.create(customerOrderRequest);
    }
}
