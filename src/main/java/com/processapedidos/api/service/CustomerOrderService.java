package com.processapedidos.api.service;

import com.processapedidos.api.dtos.CustomerOrderRequest;

public interface CustomerOrderService {
    void create(CustomerOrderRequest customerOrderRequest);
}
