package com.ecommerce.user.product.service;

import com.ecommerce.user.product.model.Item;
import com.ecommerce.user.product.dto.ItemCreateRequest;
import com.ecommerce.user.product.dto.ItemUpdateRequest;

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
