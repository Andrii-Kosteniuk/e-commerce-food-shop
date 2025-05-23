package com.commondto.order;

import com.commondto.product.OrderedItemDTO;
import com.ecommerce.deliveryservice.model.DeliveryInfo;
import com.ecommerce.order.model.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record OrderUpdateRequest (
        List<OrderedItemDTO> items,
        BigDecimal totalPrice,
        OrderStatus status,
        DeliveryInfo deliveryInfo
) {


}
