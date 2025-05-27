package com.ecommerce.order.service.impl;

import com.commondto.product.ItemResponse;
import com.ecommerce.order.feign.FeignItemClient;
import com.ecommerce.order.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final FeignItemClient itemClient;


    @Override
    public List<ItemResponse> getAllItems() {
        return itemClient.getAllItems();
    }

    @Override
    public ItemResponse getItemById(Long itemId) {
        return itemClient.getItemById(itemId);
    }

    @Override
    public ItemResponse getItemByName(String name) {
        return itemClient.findItemByName(name);
    }
}
