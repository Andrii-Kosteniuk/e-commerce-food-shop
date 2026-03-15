package com.ecommerce.product.service.impl;

import com.ecommerce.commonexception.exception.ResourceNotFoundException;
import com.ecommerce.product.model.Category;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.CatalogRepository;
import com.ecommerce.product.service.ProductCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCatalogServiceImpl implements ProductCatalogService {

    public final CatalogRepository catalogRepository;

    @Override
    public Product getProductById(Long id) {
        return catalogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Product with id %s not found", id)));
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = catalogRepository.findAll();
        if (products.isEmpty()) {
            return Collections.emptyList();
        }
        return products;
    }

    @Override
    public Product getProductByName(String name) {
        return catalogRepository.getProductByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Product with name %s not found", name)));
    }

    @Override
    public List<Product> getProductsByCategoryName(Category category) {
        return catalogRepository.getProductsByCategory(category);
    }

    @Override
    public List<Product> searchProducts(Category category) {
        return catalogRepository.getProductsByCategory(category);
    }
}
