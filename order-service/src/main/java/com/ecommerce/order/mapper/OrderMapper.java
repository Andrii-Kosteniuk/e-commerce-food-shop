package com.ecommerce.order.mapper;

import com.commondto.order.OrderCreateRequest;
import com.ecommerce.order.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order toOrder(OrderCreateRequest orderCreateRequest);
    OrderCreateRequest toOrderRequest(Order order);
}
