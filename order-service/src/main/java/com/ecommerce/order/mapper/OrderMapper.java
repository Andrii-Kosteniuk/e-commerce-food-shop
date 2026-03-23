package com.ecommerce.order.mapper;

import com.ecommerce.commondto.order.OrderItemResponse;
import com.ecommerce.commondto.order.OrderResponse;
import com.ecommerce.commondto.order.OrderRequest;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.OrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toOrderResponse(Order order);
    List<OrderItemResponse> toOrderItemResponses(List<OrderItem> items);
}
