package com.ecommerce.payment.mapper;

import com.ecommerce.commondto.payment.PaymentResponse;
import com.ecommerce.payment.model.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

        PaymentResponse toPaymentResponse(Payment payment);

}
