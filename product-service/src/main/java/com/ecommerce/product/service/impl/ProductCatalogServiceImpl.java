package com.ecommerce.product.service.impl;

import com.ecommerce.commondto.product.ProductResponse;
import com.ecommerce.commonexception.exception.ResourceNotFoundException;
import com.ecommerce.product.cache.RestPage;
import com.ecommerce.product.model.Category;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.service.ProductCatalogService;
import com.ecommerce.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCatalogServiceImpl implements ProductCatalogService {

    private final ProductRepository productRepository;
    public final ProductMapper productMapper;

    @Override
    @Cacheable(value = "products", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ProductResponse> getProducts(Pageable pageable) {
        Page<ProductResponse> page = productRepository.findAll(pageable)
                .map(productMapper::toProductResponse);

        return new RestPage<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }

    @Override
    public ProductResponse getProductByName(String name) {
        return productRepository.getProductByName(name)
                .map(productMapper::toProductResponse)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Product with name %s not found", name)));
    }

    @Override
    public List<Product> getProductsByCategoryName(Category category) {
        return productRepository.getProductsByCategory(category);
    }

    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Product with id %s not found", id)));
    }

}
