package com.ecommerce.notification.mail;

import com.ecommerce.commondto.order.OrderItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmailService emailService;


    public void notifyOrderCreated(String email, Long orderId, String total, List<OrderItemResponse> items, String url) {
        String products = items.stream()
                .map(res -> {
                    String name = res.productName();
                    BigDecimal price = res.price();
                    int quantity = res.quantity();
                    return name + ": " + quantity + " x " + price.toString();
                })
                .collect(Collectors.joining("\n"));


        emailService.sendEmail(
                email,
                "Order #" + orderId + " received",
                String.format("""
                        Thank you for your order!
                        
                        Order ID:    #%d
                        Status:      %s
                        Products:    %s
                        Total:       %s
                        
                        Thank you for shopping with Food Shop!
                        """,orderId, "NEW", products, total +  "\n" +

                "We would like you to confirm your order \n"  ) +
                """ 
                \n
                If you agree and everything is fine please click the button below
                """
                +
                // Suppose it is the button
                url
                + "\n" + "\n" +
                """
                We hope you enjoy your good !
                Thank you for shopping with us.
                """
        );

    }

    public void notifyPaymentSucceeded(String email, Long orderId, String total) {
        emailService.sendEmail(
                email,
                "Payment confirmed — Order #" + orderId,
                String.format("""
                        Your payment has been confirmed!
                        
                        Order ID:    #%d
                        Amount:      %s
                        Status:      CONFIRMED
                        
                        Your order is being prepared for delivery.
                        """, orderId, total)
        );
    }

    public void notifyPaymentFailed(String email, Long orderId, String reason) {

        emailService.sendEmail(
                email,
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
}
