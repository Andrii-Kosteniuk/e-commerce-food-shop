package com.ecommerce.order.model;

import com.ecommerce.commondto.product.ProductResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private Long id;

    @JoinColumn(name = "user_id")
    private Long userId;

    @ElementCollection
    @CollectionTable(name = "Products", joinColumns = @JoinColumn(name = "Product_id"))
    private List<ProductResponse> Products;

    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime orderCreateDate;
    private LocalDateTime orderUpdateDate;

}
