package com.ecommerce.payment.service.impl;

import com.ecommerce.commondto.kafka.PaymentFailedEvent;
import com.ecommerce.commondto.kafka.PaymentSucceededEvent;
import com.ecommerce.commondto.payment.PaymentRequest;
import com.ecommerce.commondto.payment.PaymentResponse;
import com.ecommerce.commonexception.exception.ResourceNotFoundException;
import com.ecommerce.commonexception.exception.StripePaymentException;
import com.ecommerce.kafka.producers.PaymentEventPublisher;
import com.ecommerce.payment.mapper.PaymentMapper;
import com.ecommerce.payment.mock.StripeGateway;
import com.ecommerce.payment.model.Payment;
import com.ecommerce.payment.model.PaymentStatus;
import com.ecommerce.payment.repository.PaymentRepository;
import com.ecommerce.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventPublisher paymentEventPublisher;
    private final StripeGateway stripeGateway;
    private final PaymentMapper paymentMapper;


    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        Optional<Payment> paymentByIdempotencyKey = paymentRepository.findByIdempotencyKey(request.idempotencyKey());

        if (paymentByIdempotencyKey.isPresent())
            return paymentMapper.toPaymentResponse(paymentByIdempotencyKey.get());



        Payment payment = Payment.builder()
                .userId(request.userId())
                .orderId(request.orderId())
                .amount(request.amount())
                .currency(request.currency())
                .idempotencyKey(request.idempotencyKey())
                .status(PaymentStatus.PENDING)
                .build();

        return paymentMapper.toPaymentResponse(paymentRepository.save(payment));
    }

    @Override
    @Transactional
    public PaymentResponse  confirmPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException("Payment is not in PENDING status: " + payment.getStatus());
        }

        try {
            String stripePaymentId = stripeGateway.processPayment(payment.getCurrency(), payment.getAmount());
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setStripePaymentId(stripePaymentId);

            log.info("Payment {} completed successfully. Stripe ID: {}", paymentId, stripePaymentId);

            paymentRepository.save(payment);

            paymentEventPublisher.publishPaymentSucceeded(
                    new PaymentSucceededEvent(
                            paymentId,
                            payment.getOrderId(),
                            payment.getUserId(),
                            stripePaymentId
                    )
            );
        } catch (StripePaymentException e) {
            payment.setStatus(PaymentStatus.FAILED);
            log.error("Payment {} failed. Reason: {}", paymentId, e.getMessage());

            paymentRepository.save(payment);

            paymentEventPublisher.publishPaymentFailed(
                    new PaymentFailedEvent(
                            paymentId,
                            payment.getOrderId(),
                            payment.getUserId(),
                            e.getMessage()
            ));
        }

        return paymentMapper.toPaymentResponse(payment);
    }

    @Override
    public PaymentResponse getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for order ID: " + orderId));

        return paymentMapper.toPaymentResponse(payment);
    }
}
