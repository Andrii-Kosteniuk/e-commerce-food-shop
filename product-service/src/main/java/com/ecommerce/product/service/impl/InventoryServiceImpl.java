package com.ecommerce.product.service.impl;

import com.ecommerce.commonexception.exception.InsufficientStockException;
import com.ecommerce.commonexception.exception.ResourceNotFoundException;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.service.InventoryService;
import com.ecommerce.product.service.ProductCatalogService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final ProductRepository productRepository;
    private final ProductCatalogService productCatalogService;

    @Override
    @Transactional
    @CacheEvict(value = "products", key = "#productId")
    public void decreaseStock(long productId, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity to decrease must be positive, got: " + quantity);
        }

        int updatedRows = productRepository.decreaseStock(productId, quantity);

        if (updatedRows == 0) {
            boolean exists = productRepository.existsById(productId);
            if (!exists) {
                throw new ResourceNotFoundException(
                        String.format("Product with id '%d' not found", productId));
            }
            throw new InsufficientStockException(
                    String.format(
                            "Insufficient stock for product id '%d'. Requested: %d",
                            productId, quantity));
        }

        log.info("Decreased stock for product {} by {}", productId, quantity);

    }

    @Override
    @Transactional
    @CacheEvict(value = "products", key = "#productId")
    public void increaseStock(long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity to increase must be positive, got: " + quantity);
        }

        int updatedRows = productRepository.increaseStock(productId, quantity);

        if (updatedRows == 0) {
            throw new ResourceNotFoundException(
                    String.format("Product with id '%d' not found", productId));
        }

        log.info("Increased stock for product {} by {}", productId, quantity);
    }
}
