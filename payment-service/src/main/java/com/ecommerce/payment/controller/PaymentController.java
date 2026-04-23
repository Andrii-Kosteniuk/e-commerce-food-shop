package com.ecommerce.payment.controller;

import com.ecommerce.commondto.payment.PaymentResponse;
import com.ecommerce.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

//    @PostMapping
//    public ResponseEntity<PaymentResponse> createPayment(
//            @Valid @RequestBody PaymentRequest request,
//            @RequestHeader("X-User-Id") Long userId) {
//
//        log.info("Creating payment for orderId= {}, userId={}", request.orderId(), request.userId());
//        PaymentResponse payment = paymentService.createPayment(request, userId);
//
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(payment);
//    }

    @PostMapping("/{paymentId}/confirm")
    public ResponseEntity<PaymentResponse> confirmPayment (
            @PathVariable Long paymentId,
            @RequestHeader("X-User-Id") Long userId) {

        log.info("Confirming payment id={}", paymentId);
        return ResponseEntity.ok(paymentService.confirmPayment(paymentId, userId));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId (@PathVariable Long orderId) {

        log.info("Fetching payment for orderId={}", orderId);
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }



}
