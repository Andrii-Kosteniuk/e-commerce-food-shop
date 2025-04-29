package com.ecommerce.product.controller;


import com.ecommerce.product.model.Item;
import com.ecommerce.product.model.ItemMapper;
import com.ecommerce.product.model.ItemRequest;
import com.ecommerce.product.model.ItemResponse;
import com.ecommerce.product.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;


    @PostMapping("/create")
    public ResponseEntity<ItemResponse> createItem(@RequestBody @Valid ItemRequest request) {
        ItemResponse itemResponse = itemMapper.toItemResponse(itemService.createItem(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(itemResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> updateItem(@PathVariable Long id, @RequestBody @Valid ItemRequest request) {
        ItemResponse itemResponse = itemMapper.toItemResponse(itemService.updateItem(id, request));
        return ResponseEntity.ok(itemResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItem(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @GetMapping("/name")
    public ResponseEntity<ItemResponse> getItemByName(@RequestBody ItemRequest request) {
        Optional<Item> itemByName = itemService.findItemByName(request.name());
        if (itemByName.isPresent()) {
            ItemResponse itemResponse = itemMapper.toItemResponse(itemByName.get());
            return ResponseEntity.ok(itemResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/category")
    public ResponseEntity<List<ItemResponse>> getItemsByCategory(@RequestBody ItemRequest request) {
        List<ItemResponse> itemResponses = itemService.getAllItems()
                .stream()
                .filter(item -> item.getCategory().equals(request.category()))
                .map(itemMapper::toItemResponse)
                .toList();
        return ResponseEntity.ok(itemResponses);
    }
}
