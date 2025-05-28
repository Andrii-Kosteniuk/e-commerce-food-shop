package com.ecommerce.order.mapper;

import com.ecommerce.commondto.order.OrderResponse;
import com.ecommerce.commondto.order.OrderRequest;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper {


    @Mapping(target = "status", resultType = OrderStatus.class)
    OrderResponse toOrderResponse(Order order);
}
