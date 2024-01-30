package com.processapedidos.api.service;

import com.processapedidos.api.dtos.CustomerOrderRequest;
import com.processapedidos.api.model.CustomerOrder;
import com.processapedidos.api.model.Member;
import com.processapedidos.api.model.OrderItem;
import com.processapedidos.api.model.Product;
import com.processapedidos.api.repository.CustomerOrderRepository;
import com.processapedidos.api.repository.MemberRepository;
import com.processapedidos.api.repository.OrderItemRepository;
import com.processapedidos.api.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CustomerOrderServiceImpl implements CustomerOrderService {

    @Autowired
    CustomerOrderRepository customerOrderRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Override
    @Transactional
    public void create(CustomerOrderRequest customerOrderRequest) {
        Member member = memberRepository.findById(customerOrderRequest.getMemberId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Membro não encontrado"));

        CustomerOrder savedCustomerOrder = customerOrderRepository.save(new CustomerOrder(
                UUID.randomUUID(),
                member,
                LocalDateTime.now().toLocalDate(),
                null,
                null
        ));

        AtomicReference<BigDecimal> totalOrderRef = new AtomicReference<>(BigDecimal.ZERO);

        for (OrderItem orderItem : customerOrderRequest.getOrderItemSet()) {
            Optional<Product> productFromOrder = productRepository.findById(orderItem.getProduct().getId());
            productFromOrder.ifPresentOrElse(
                    product -> {
                        totalOrderRef.updateAndGet(currentTotal -> currentTotal.add(product.getPrice()
                                .multiply(BigDecimal.valueOf(orderItem.getQuantity()))));
                        orderItem.setCustomerOrder(savedCustomerOrder);
                        orderItemRepository.save(orderItem);
                    },
                    () -> {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado.");
                    }
            );
        }

        savedCustomerOrder.setTotal(totalOrderRef.get());

        customerOrderRepository.save(savedCustomerOrder);
    }
}
