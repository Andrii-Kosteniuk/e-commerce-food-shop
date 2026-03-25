package com.ecommerce.product.controller;

import com.ecommerce.commondto.product.ProductResponse;
import com.ecommerce.product.model.Category;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.service.ProductCatalogService;
import com.ecommerce.product.util.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Page<ProductResponse>> retrieveAllProducts(@PageableDefault(size = 20, sort = "name") Pageable pageable) {
        Page<ProductResponse> productPageResponse = catalogService.getProducts(pageable);
        return ResponseEntity.ok(productPageResponse);
    }

    @GetMapping("/name")
    public ResponseEntity<ProductResponse> retrieveProductByName(@RequestParam("name") String name) {
        ProductResponse product = catalogService.getProductByName(name);
        return ResponseEntity.ok(product);
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
