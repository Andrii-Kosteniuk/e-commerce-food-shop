package com.ecommerce.notification.mail;

import com.ecommerce.commondto.order.OrderItemResponse;
import com.ecommerce.commondto.user.UserResponse;
import com.ecommerce.notification.UserServiceFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmailService emailService;
    private final UserServiceFeignClient userServiceFeignClient;


    public void notifyOrderCreated(String email, Long orderId, String total, List<OrderItemResponse> items, String url) {

        String productsBlock = formatProducts(items);

        String body = buildOrderCreatedEmail(orderId, total, productsBlock, url);

        emailService.sendEmail(
                email,
                "Order #" + orderId + " — Confirmation Required",
                body
        );

    }

    public void notifyPaymentSucceeded(Long userId, Long orderId) {
        UserResponse userById = userServiceFeignClient.getUserById(userId);

        emailService.sendEmail(
                userById.email(),
                "Payment completed — Order #" + orderId,

                String.format("""
                        Your payment has been completed!
                        
                        Order ID:    #%d
                        Status:      COMPLETED
                        
                        Your order is being prepared for delivery.
                        """, orderId)
        );
    }

    public void notifyOrderCanceled(Long userId, Long orderId) {

        UserResponse userById = userServiceFeignClient.getUserById(userId);
        emailService.sendEmail(
                userById.email(),
                "Order #" + orderId + " — Canceled",

                String.format("""
                        Your order has been canceled!
                        
                        Order ID:    #%d
                        Status:      CANCELED
                        
                        """, orderId)
        );
    }

    public void notifyPaymentFailed(Long userId, Long orderId, String reason) {
        UserResponse userById = userServiceFeignClient.getUserById(userId);
        emailService.sendEmail(
                userById.email(),
                "Payment failed — Order #" + orderId,

                String.format("""
                        Unfortunately your payment could not be processed.
                        
                        Order ID:    #%d
                        Reason:      %s
                        Status:      CANCELLED
                        
                        Please try placing your order again.
                        """, orderId, reason)
        );
    }

    private String formatProducts(List<OrderItemResponse> items) {
        return items.stream()
                .map(item -> String.format(
                        "- %-35s %d × %s",
                        item.productName(),
                        item.quantity(),
                        item.price()
                ))
                .collect(Collectors.joining("\n"));
    }

    private String buildOrderCreatedEmail(Long orderId, String total, String products, String url) {

        return String.format("""
                Hello,
                
                Thank you for your order.
                
                Order Summary
                ----------------------------------------
                Order ID:   #%d
                Status:     NEW
                
                Products:
                %s
                
                ----------------------------------------
                Total:      %s
                ----------------------------------------
                
                To proceed with your order, please confirm it using the link below:
                
                Confirm your order:
                %s
                
                If you did not place this order, you can ignore this email.
                
                Thank you,
                Food Shop Team
                """,
                orderId,
                products,
                total,
                url
        );
    }

}
