package com.ecommerce.product.controller;

import com.ecommerce.product.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class InventoryAdminController {

    public final InventoryService inventoryService;

    @PatchMapping("/inventory/{productId}/decrease")
    void decreaseStock(@PathVariable (name = "productId") Long productId, @RequestParam(name = "quantity") int quantity) {
        inventoryService.decreaseStock(productId, quantity);

    }

    @PatchMapping("/inventory/{productId}/increase")
    void increaseStock(@PathVariable (name = "productId") Long productId, @RequestParam(name = "quantity") int quantity) {
        inventoryService.increaseStock(productId, quantity);

    }
}
