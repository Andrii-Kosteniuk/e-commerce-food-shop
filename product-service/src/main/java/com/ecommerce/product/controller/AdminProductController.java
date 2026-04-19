package com.ecommerce.product.controller;

import com.ecommerce.commondto.product.ProductCreateRequest;
import com.ecommerce.commondto.product.ProductResponse;
import com.ecommerce.commondto.product.ProductUpdateRequest;
import com.ecommerce.product.service.ProductManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/products")
public class AdminProductController {

    private final ProductManagementService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        ProductResponse productResponse = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequest request) {
        ProductResponse productResponse = productService.updateProduct(id, request);
        return ResponseEntity.ok(productResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
