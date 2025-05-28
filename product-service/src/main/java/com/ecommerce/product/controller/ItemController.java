package com.ecommerce.product.controller;

import com.ecommerce.commondto.product.ItemCreateRequest;
import com.ecommerce.commondto.product.ItemResponse;
import com.ecommerce.commondto.product.ItemUpdateRequest;
import com.ecommerce.product.model.Category;
import com.ecommerce.product.model.Item;
import com.ecommerce.product.service.ItemService;
import com.ecommerce.product.util.ItemMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
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

    @GetMapping("/item/{name}")
    public ResponseEntity<ItemResponse> getItemByName(@PathVariable("name") String name) {
        Item item = itemService.getItemByName(name);
        return ResponseEntity.ok(itemMapper.toItemResponse(item));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable("id") Long itemId) {
        Item item = itemService.getItemById(itemId);
        return ResponseEntity.ok(itemMapper.toItemResponse(item));
    }

    @GetMapping("/category/{name}")
    public ResponseEntity<List<ItemResponse>> getItemsByCategory(@PathVariable("name") Category category) {
        List<ItemResponse> itemResponses = itemService.getItemsByCategoryName(category)
                .stream()
                .map(itemMapper::toItemResponse)
                .toList();
        return ResponseEntity.ok(itemResponses);
    }

    @PutMapping("/update-stock/{itemId}/quantity/{quantity}")
    void reduceItemStock(@PathVariable Long itemId, @PathVariable int quantity) {
        itemService.reduceItemStock(itemId, quantity);

    }

}
