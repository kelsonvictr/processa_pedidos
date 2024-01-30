package com.processapedidos.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.processapedidos.api.dtos.PaymentRequest;
import com.processapedidos.api.enums.MembershipStatusEnum;
import com.processapedidos.api.enums.ProductTypeEnum;
import com.processapedidos.api.enums.ShippingGuideTypeEnum;
import com.processapedidos.api.model.*;
import com.processapedidos.api.repository.*;
import com.processapedidos.api.utils.EmailService;
import com.processapedidos.api.utils.PaymentMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final String EMAIL_SUBJECT = "Adesão/Upgrade confirmada com sucesso!";

    private static final String EMAIL_TEXT = "Olá! \n Sua ativação/upgrade foi efetivada!";

    private static final BigDecimal COMMISSION_AMOUNT = new BigDecimal("100.00");

    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final PaymentMessageSender paymentMessageSender;
    private final OrderItemRepository orderItemRepository;
    private final ShippingGuideRepository shippingGuideRepository;
    private final CommissionRepository commissionRepository;
    private final AgentRepository agentRepository;
    private final EmailService emailService;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              MemberRepository memberRepository,
                              CustomerOrderRepository customerOrderRepository,
                              PaymentMessageSender paymentMessageSender,
                              OrderItemRepository orderItemRepository,
                              ShippingGuideRepository shippingGuideRepository,
                              CommissionRepository commissionRepository,
                              AgentRepository agentRepository,
                              EmailService emailService) {
        this.paymentRepository = paymentRepository;
        this.memberRepository = memberRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.paymentMessageSender = paymentMessageSender;
        this.orderItemRepository = orderItemRepository;
        this.shippingGuideRepository = shippingGuideRepository;
        this.commissionRepository = commissionRepository;
        this.agentRepository = agentRepository;
        this.emailService = emailService;
    }

    @Override
    public void create(PaymentRequest paymentRequest) throws JsonProcessingException {
        Member member = memberRepository.findById(paymentRequest.getMemberId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Membro não encontrado"));

        CustomerOrder customerOrder = customerOrderRepository.findById(paymentRequest.getCustomerOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compra do cliente não encontrada"));

        Payment payment = new Payment(
                UUID.randomUUID(),
                member,
                customerOrder,
                LocalDate.now(),
                false,
                customerOrder.getTotal()
        );

        boolean isSuccessful = paymentProcessSender(payment);

        if (isSuccessful != payment.getIsSuccessful()) payment.setIsSuccessful(isSuccessful);

        paymentRepository.save(payment);
    }

    @Override
    public void processReceivedPayments(Payment payment) {
        List<Product> productList = orderItemRepository.findProductsByCustomerOrderId(payment.getCustomerOrder().getId());

        if (hasPhysicalProduct(productList)) {
            shippingGuideRepository.save(new ShippingGuide(
                    UUID.randomUUID(),
                    payment.getCustomerOrder(),
                    LocalDate.now(),
                    ShippingGuideTypeEnum.TO_SHIPPING
            ));

            Agent agent = agentRepository.findAll().get(0);

            commissionRepository.save(
                    new Commission(
                            UUID.randomUUID(),
                            agent,
                            payment.getCustomerOrder(),
                            COMMISSION_AMOUNT
                    )
            );
        }

        if (hasBookProduct(productList)) {
            shippingGuideRepository.save(new ShippingGuide(
                    UUID.randomUUID(),
                    payment.getCustomerOrder(),
                    LocalDate.now(),
                    ShippingGuideTypeEnum.TO_ROYALTIES
            ));
            shippingGuideRepository.save(new ShippingGuide(
                    UUID.randomUUID(),
                    payment.getCustomerOrder(),
                    LocalDate.now(),
                    ShippingGuideTypeEnum.TO_ROYALTIES
            ));

            Agent agent = agentRepository.findAll().get(0);

            commissionRepository.save(
                    new Commission(
                            UUID.randomUUID(),
                            agent,
                            payment.getCustomerOrder(),
                            COMMISSION_AMOUNT
                    )
            );
        }

        if (hasNewAssociationMember(productList)) {
            Member member = payment.getMember();
            if (member.getMembershipStatus() != MembershipStatusEnum.ACTIVE) {
                member.setMembershipStatus(MembershipStatusEnum.ACTIVE);
                memberRepository.save(member);
            }
        }

        if (hasMembershipOrUpgrade(productList)) {
            emailService.sendSimpleMessage(payment.getMember().getEmail(), EMAIL_SUBJECT, EMAIL_TEXT);
        }

        if (hasVideo(productList) && productList.stream().anyMatch(product -> product.getOthersDetails().contains("\"bonus\":"))) {
            shippingGuideRepository.save(new ShippingGuide(
                    UUID.randomUUID(),
                    payment.getCustomerOrder(),
                    LocalDate.now(),
                    ShippingGuideTypeEnum.HAS_BONUS
            ));
        }

    }

    private boolean paymentProcessSender(Payment payment) throws JsonProcessingException {
        paymentMessageSender.sendPayment(payment);

        return true;
    }

    private Boolean hasPhysicalProduct(List<Product> productList) {
        return productList.stream()
                .anyMatch(product -> ProductTypeEnum.PHYSICAL.equals(product.getType()));
    }

    private Boolean hasBookProduct(List<Product> productList) {
        return productList.stream()
                .anyMatch(product -> ProductTypeEnum.BOOK.equals(product.getType()));
    }

    private Boolean hasNewAssociationMember(List<Product> productList) {
        return productList.stream()
                .anyMatch(product -> ProductTypeEnum.NEW_ASSOCIATION_MEMBER.equals(product.getType()));
    }

    private Boolean hasMembershipOrUpgrade(List<Product> productList) {
        return productList.stream()
                .anyMatch(product -> ProductTypeEnum.MEMBERSHIP_OR_UPGRADE.equals(product.getType()));
    }

    private Boolean hasVideo(List<Product> productList) {
        return productList.stream()
                .anyMatch(product -> ProductTypeEnum.VIDEO.equals(product.getType()));
    }
}
