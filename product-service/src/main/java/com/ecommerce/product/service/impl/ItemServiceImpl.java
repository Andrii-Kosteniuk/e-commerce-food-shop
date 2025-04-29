package com.ecommerce.product.service.impl;

import com.ecommerce.product.model.Item;
import com.ecommerce.product.model.ItemMapper;
import com.ecommerce.product.model.ItemRequest;
import com.ecommerce.product.repository.ItemRepository;
import com.ecommerce.product.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public Item createItem(ItemRequest itemRequest) {
        Item foundItem = itemRepository.findByName(itemRequest.name());
        if (! Objects.isNull(foundItem)) {
            throw new IllegalArgumentException("Item with this name already exists");
        } else {
            Item item = itemMapper.itemRequestToItem(itemRequest);
            return itemRepository.save(item);
        }
    }

    @Override
    public Item updateItem(Long id, ItemRequest itemRequest) {
        Item foundItem = getItemById(id);

        foundItem.setName(itemRequest.name());
        foundItem.setPrice(itemRequest.price());
        foundItem.setQuantity(itemRequest.quantity());
        foundItem.setCategory(itemRequest.category());
        foundItem.setImageUrl(itemRequest.imageUrl());
        foundItem.setCreatedAt(itemRequest.createdAt());

        return itemRepository.save(foundItem);

    }

    @Override
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public Item getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));
    }

    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public Optional<Item> findItemByName(String name) {
        return Optional.ofNullable(itemRepository.findByName(name));
    }

    @Override
    public List<Item> findItemsByCategory(Item.Category category) {
        return itemRepository.findByCategory(category);
    }
}
