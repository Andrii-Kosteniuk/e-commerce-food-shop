package com.ecommerce.user.product.controller;

import com.ecommerce.user.product.dto.ItemCreateRequest;
import com.ecommerce.user.product.dto.ItemResponse;
import com.ecommerce.user.product.dto.ItemUpdateRequest;
import com.ecommerce.user.product.model.Item;
import com.ecommerce.user.product.service.ItemService;
import com.ecommerce.user.product.util.ItemMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @PostMapping("/create")
    public ResponseEntity<ItemResponse> createItem(@RequestBody @Valid ItemCreateRequest request) {
        ItemResponse itemResponse = itemMapper.toItemResponse(itemService.createItem(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(itemResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> updateItem(@PathVariable Long id, @RequestBody @Valid ItemUpdateRequest request) {
        ItemResponse itemResponse = itemMapper.toItemResponse(itemService.updateItem(id, request));
        return ResponseEntity.ok(itemResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAllItems() {
        List<ItemResponse> itemResponses = itemService.getAllItems()
                .stream()
                .map(itemMapper::toItemResponse)
                .toList();

        return ResponseEntity.ok(itemResponses);
    }

    @GetMapping("/name")
    public ResponseEntity<ItemResponse> getItemByName(@RequestParam String name) {
        return itemService.findItemByName(name)
                .map(item -> ResponseEntity.ok(itemMapper.toItemResponse(item)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category")
    public ResponseEntity<List<ItemResponse>> getItemsByCategory(@RequestParam Item.Category category) {
        List<ItemResponse> itemResponses = itemService.findItemsByCategory(category)
                .stream()
                .map(itemMapper::toItemResponse)
                .toList();
        return ResponseEntity.ok(itemResponses);
    }
}
