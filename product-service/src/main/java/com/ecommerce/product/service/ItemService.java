package com.ecommerce.product.service;

import com.ecommerce.product.model.Item;
import com.ecommerce.product.dto.ItemCreateRequest;
import com.ecommerce.product.dto.ItemUpdateRequest;

import java.util.List;
import java.util.Optional;

public interface ItemService {
        Item createItem(ItemCreateRequest itemCreateRequest);
        Item updateItem(Long id, ItemUpdateRequest itemUpdateRequest);
        void deleteItem(Long id);
        Optional<Item> getItemById(Long id);
        List<Item> getAllItems();
        Optional<Item> findItemByName(String name);
        List<Item> findItemsByCategory(Item.Category category);

}
