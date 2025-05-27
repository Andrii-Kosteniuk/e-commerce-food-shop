package com.ecommerce.product.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean isAvailable;
}
