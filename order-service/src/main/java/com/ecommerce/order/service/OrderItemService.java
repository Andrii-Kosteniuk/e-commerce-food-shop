package com.ecommerce.order.service;

import com.ecommerce.commondto.product.ItemResponse;

import java.util.List;

public interface OrderItemService {

    List<ItemResponse> getAllItems();
    ItemResponse getItemById(Long itemId);
    ItemResponse getItemByName(String name);
    void reduceItemStock(Long itemId, int quantity);

}
