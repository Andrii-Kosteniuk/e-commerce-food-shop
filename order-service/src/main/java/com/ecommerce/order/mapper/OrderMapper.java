package com.ecommerce.order.mapper;

import com.commondto.order.OrderResponse;
import com.commondto.order.OrderRequest;
import com.ecommerce.order.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    Order toOrder(OrderRequest orderResponse);

    OrderResponse toOrderResponse(Order order);
}
