package com.ecommerce.product.service;

import com.ecommerce.product.model.Item;
import com.ecommerce.product.model.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface ItemService {
        Item createItem(ItemRequest itemRequest);
        Item updateItem(Long id, ItemRequest itemRequest);
        void deleteItem(Long id);
        Item getItemById(Long id);
        List<Item> getAllItems();
        Optional<Item> findItemByName(String name);
        List<Item> findItemsByCategory(Item.Category category);

}
