package com.ecommerce.product.service.impl;

import com.commonexception.exception.ResourceAlreadyExistsException;
import com.commonexception.exception.ResourceNotFoundException;
import com.commondto.product.ItemCreateRequest;
import com.commondto.product.ItemUpdateRequest;
import com.ecommerce.product.model.Category;
import com.ecommerce.product.model.Item;
import com.ecommerce.product.repository.ItemRepository;
import com.ecommerce.product.service.ItemService;
import com.ecommerce.product.util.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public Item createItem(ItemCreateRequest itemCreateRequest) {
        itemRepository.findByName(itemCreateRequest.name()).ifPresent(item -> {
            throw new ResourceAlreadyExistsException(
                    String.format("Item with name %s already exists", itemCreateRequest.name()));
        });

        Item item = itemMapper.itemRequestToItem(itemCreateRequest);
        item.setCreatedAt(LocalDateTime.now());
        item.setAvailable(true);
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Long id, ItemUpdateRequest itemUpdateRequest) {
        Item existingItem = getItemById(id);

        Category newCategory = Arrays.stream(Category.values())
                .filter(category -> category.name().equalsIgnoreCase(itemUpdateRequest.category()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        existingItem.setName(itemUpdateRequest.name());
        existingItem.setPrice(itemUpdateRequest.price());
        existingItem.setCategory(newCategory);
        existingItem.setImageUrl(itemUpdateRequest.imageUrl());
        existingItem.setUpdatedAt(LocalDateTime.now());

        return itemRepository.save(existingItem);
    }

    @Override
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public Item getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Item with id %d not found", id)));
    }

    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public Item getItemByName(String name) {
        return itemRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Item with name %s not found", name)));
    }

    @Override
    public List<Item> getItemsByCategoryName(Category category) {
        return itemRepository.findItemsByCategory(category);
    }
}