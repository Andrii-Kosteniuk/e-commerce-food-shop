package com.ecommerce.product.service;

import com.ecommerce.product.model.Category;
import com.ecommerce.product.model.Item;
import com.commondto.product.ItemCreateRequest;
import com.commondto.product.ItemUpdateRequest;

import java.util.List;

public interface ItemService {
    Item createItem(ItemCreateRequest itemCreateRequest);
    Item updateItem(Long id, ItemUpdateRequest itemUpdateRequest);
    void deleteItem(Long id);
    Item getItemById(Long id);
    List<Item> getAllItems();
    Item getItemByName(String name);
    List<Item> getItemsByCategoryName(Category category);

}
