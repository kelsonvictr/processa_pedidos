package com.processapedidos.api.service;

import com.processapedidos.api.dtos.PaymentRequest;
import com.processapedidos.api.model.*;
import com.processapedidos.api.repository.*;
import com.processapedidos.api.utils.PaymentMessageSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private CustomerOrderRepository customerOrderRepository;
    @Mock
    private PaymentMessageSender paymentMessageSender;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ShippingGuideRepository shippingGuideRepository;

    @Mock
    private CommissionRepository commissionRepository;

    @Mock
    private AgentRepository agentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void testCreate() throws Exception {
        PaymentRequest request = new PaymentRequest();
        Member member = new Member();
        CustomerOrder order = new CustomerOrder();
        when(memberRepository.findById(any())).thenReturn(java.util.Optional.of(member));
        when(customerOrderRepository.findById(any())).thenReturn(java.util.Optional.of(order));
        doNothing().when(paymentMessageSender).sendPayment(any(Payment.class));

        paymentService.create(request);

        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testProcessReceivedPayments() {
        Payment payment = new Payment();
        payment.setCustomerOrder(new CustomerOrder());
        payment.getCustomerOrder().setId(UUID.randomUUID());

        List<Product> products = List.of(new Product());
        when(orderItemRepository.findProductsByCustomerOrderId(any())).thenReturn(products);

        paymentService.processReceivedPayments(payment);

    }


}
