package com.commondto.order;

import com.commondto.product.ItemResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private Long userId;
    private List<ItemResponse> items;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime orderCreateDate;
    private LocalDateTime orderUpdateDate;

}
