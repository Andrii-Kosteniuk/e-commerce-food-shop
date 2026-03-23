package com.ecommerce.order.service.impl;

import com.ecommerce.commondto.order.OrderRequest;
import com.ecommerce.commondto.order.OrderResponse;
import com.ecommerce.commondto.user.UserResponse;
import com.ecommerce.order.feign.FeignProductClient;
import com.ecommerce.order.feign.FeignUserClient;
import com.ecommerce.order.mapper.OrderMapper;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.service.OrderModifiedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderModifiedServiceImpl implements OrderModifiedService {

    private final OrderRepository orderRepository;
    private final FeignUserClient userClient;
    private final FeignProductClient productClient;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponse createOrder(Long id, OrderRequest request) {

        UserResponse user = userClient.getUserById(id);

        List<OrderItem> items = request.products().stream()
                .map(product -> {
                    var productInfo = productClient.getProductById(product.id());
                    log.info("Product is {}", productInfo);
                    return OrderItem.builder()
                            .productId(productInfo.id())
                            .productName(productInfo.name())
                            .price(productInfo.price())
                            .quantity(product.quantity())
                            .build();
                })
                        .toList();

        BigDecimal total = items.stream()
                .map(orderItem -> orderItem.getPrice()
                        .multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);



        Order order = Order.builder()
                .userId(user.id())
                .items(items)
                .totalPrice(total)
                .status(OrderStatus.NEW)
                .orderCreateDate(LocalDateTime.now())
                .orderUpdateDate(LocalDateTime.now())
                .build();

        items.forEach(item -> item.setOrder(order));
        orderRepository.save(order);

        return orderMapper.toOrderResponse(order);
    }

    @Override
    public Order updateOrder(Long orderId, OrderRequest orderRequest) {
        return null;
    }

    @Override
    public void deleteOrder(Long id) {

    }
}
