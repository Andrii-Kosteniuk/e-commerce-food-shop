package com.ecommerce.product.service.impl;

import com.commonexception.exception.ResourceAlreadyExistsException;
import com.commonexception.exception.ResourceNotFoundException;
import com.ecommerce.product.dto.ItemCreateRequest;
import com.ecommerce.product.dto.ItemUpdateRequest;
import com.ecommerce.product.model.Item;
import com.ecommerce.product.repository.ItemRepository;
import com.ecommerce.product.service.ItemService;
import com.ecommerce.product.util.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        Item existingItem = getItemById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("%s not found", itemUpdateRequest.name())));

        Item.Category newCategory = Arrays.stream(Item.Category.values())
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
    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public Optional<Item> findItemByName(String name) {
        return itemRepository.findByName(name);
    }

    @Override
    public List<Item> getItemsByCategoryName(Item.Category category) {
        return itemRepository.findItemsByCategory(category);
    }
}