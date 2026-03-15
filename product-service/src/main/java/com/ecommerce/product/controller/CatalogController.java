package com.ecommerce.product.controller;

import com.ecommerce.commondto.product.ProductResponse;
import com.ecommerce.product.model.Category;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.service.ProductCatalogService;
import com.ecommerce.product.util.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class CatalogController {

    public final ProductCatalogService catalogService;
    public final ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> retrieveAllProducts() {
        List<ProductResponse> productResponses = catalogService.getAllProducts()
                .stream()
                .map(productMapper::toProductResponse)
                .toList();

        return ResponseEntity.ok(productResponses);
    }

    @GetMapping("/name")
    public ResponseEntity<ProductResponse> retrieveProductByName(@RequestParam("name") String name) {
        Product product = catalogService.getProductByName(name);
        return ResponseEntity.ok(productMapper.toProductResponse(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> retrieveProductById(@PathVariable("id") Long id) {
        Product product = catalogService.getProductById(id);
        return ResponseEntity.ok(productMapper.toProductResponse(product));
    }

    @GetMapping("/category")
    public ResponseEntity<List<ProductResponse>> retrieveProductsByCategory(@RequestParam Category category) {

        List<ProductResponse> productResponses = catalogService.getProductsByCategoryName(category)
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
        return ResponseEntity.ok(productResponses);
    }
}
