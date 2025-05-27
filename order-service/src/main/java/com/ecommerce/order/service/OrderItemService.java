package com.ecommerce.order.service;

import com.commondto.product.ItemResponse;

import java.util.List;

public interface OrderItemService {

    List<ItemResponse> getAllItems();
    ItemResponse getItemById(Long itemId);
    ItemResponse getItemByName(String name);
}
