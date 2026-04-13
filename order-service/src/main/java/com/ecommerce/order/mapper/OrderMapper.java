package com.ecommerce.order.mapper;

import com.ecommerce.commondto.order.OrderResponse;
import com.ecommerce.order.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderId", source = "id")
    OrderResponse toOrderResponse(Order order);
}
