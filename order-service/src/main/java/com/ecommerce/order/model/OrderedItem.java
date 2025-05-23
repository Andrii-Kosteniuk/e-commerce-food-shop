package com.ecommerce.order.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderedItem {
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private String imageUrl;
    private String category;

}
