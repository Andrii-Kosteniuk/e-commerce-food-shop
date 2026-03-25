package com.ecommerce.product.service.impl;

import com.ecommerce.commonexception.exception.ResourceNotFoundException;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final ProductRepository productRepository;
    private final ProductCatalogServiceImpl productCatalogService;


    @Override
    public void decreaseStock(long productId, int quantity) {
        Product product = productCatalogService.getProductById(productId);

        if (product.getQuantity() <= 0) {
            throw new ResourceNotFoundException("Cannot update stock: Product is already out of stock");
        }

        product.setQuantity(product.getQuantity() - quantity);
        product.setUpdatedAt(LocalDateTime.now());
        if (product.getQuantity() == 0) {
            product.setAvailable(false);
        }
        productRepository.save(product);

    }

    @Override
    public void increaseStock(long productId, int quantity) {

    }
}
