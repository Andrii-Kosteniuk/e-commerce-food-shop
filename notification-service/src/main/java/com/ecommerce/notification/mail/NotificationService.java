package com.ecommerce.notification.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmailService emailService;


    public void notifyOrderCreated(String email, Long orderId, String total) {
        emailService.sendEmail(
                email,
                "Order #" + orderId + " received",
                String.format("""
                        Thank you for your order!
                        
                        Order ID:    #%d
                        Total:       %s
                        Status:      NEW
                        
                        We are processing your payment.
                        You will receive a confirmation once payment is complete.
                        
                        Thank you for shopping with Food Shop!
                        """, orderId, total)
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

    public void notifyOrderShipped(String email, Long orderId,
                                   String estimatedDelivery) {
        emailService.sendEmail(
                email,
                "Your order is on the way — Order #" + orderId,
                String.format("""
                        Great news! Your order has been shipped.
                        
                        Order ID:            #%d
                        Estimated delivery:  %s
                        Status:              SHIPPED
                        """, orderId, estimatedDelivery)
        );
    }

    public void notifyOrderDelivered(String email, Long orderId) {
        emailService.sendEmail(
                email,
                "Order delivered — Order #" + orderId,
                String.format("""
                        Your order has been delivered!
                        
                        Order ID:    #%d
                        Status:      DELIVERED
                        
                        We hope you enjoy your food!
                        Thank you for shopping with Food Shop.
                        """, orderId)
        );
    }

}
