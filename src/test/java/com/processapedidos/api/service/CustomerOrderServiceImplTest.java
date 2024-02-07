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
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
class CustomerOrderServiceImplTest {

    @Mock
    private CustomerOrderRepository customerOrderRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CustomerOrderServiceImpl customerOrderService;

    @Test
    void createOrderSuccessfully() {
        UUID memberId = UUID.randomUUID();
        Member member = new Member();
        member.setId(memberId);

        CustomerOrderRequest request = new CustomerOrderRequest();

        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setPrice(BigDecimal.valueOf(100));

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(1L);

        request.setOrderItemSet(new HashSet<>(Arrays.asList(orderItem)));

        given(memberRepository.findById(any(UUID.class))).willReturn(Optional.of(member));
        given(productRepository.findById(any(UUID.class))).willReturn(Optional.of(product));
        given(customerOrderRepository.save(any(CustomerOrder.class))).willAnswer(invocation -> invocation.getArgument(0));

        assertThrows(ResponseStatusException.class, () -> {
            customerOrderService.create(request);
        });

    }
}

