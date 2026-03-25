package com.ecommerce.order.mapper;

import com.ecommerce.commondto.order.OrderItemResponse;
import com.ecommerce.commondto.order.OrderResponse;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toOrderResponse(Order order);
    List<OrderItemResponse> toOrderItemResponses(List<OrderItem> items);
}
