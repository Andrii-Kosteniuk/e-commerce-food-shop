package com.ecommerce.product.service.impl;

import com.ecommerce.commonexception.exception.ResourceNotFoundException;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.service.InventoryService;
import com.ecommerce.product.service.ProductCatalogService;
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
    @CacheEvict(value = "products", key = "#productId")
    public void decreaseStock(long productId, int quantity) {
        Product product = productCatalogService.getProductById(productId);

        if (product.getQuantity() <= 0) {
            throw new ResourceNotFoundException("Cannot update stock: Product is already out of stock");
        }

        product.setQuantity(product.getQuantity() - quantity);
        if (product.getQuantity() == 0) {
            product.setAvailable(false);
        }
        productRepository.save(product);

    }

    @Override
    @CacheEvict(value = "products", key = "#productId")
    public void increaseStock(long productId, int  quantity) {
        Product product = productCatalogService.getProductById(productId);

        product.setQuantity(product.getQuantity() + quantity);
        product.setAvailable(true);

        productRepository.save(product);

        log.info("Increased stock for product {} by {}, new quantity: {}",
                productId, quantity, product.getQuantity());

    }
}
