package com.ecommerce.product.service.impl;

import com.ecommerce.commondto.product.ProductCreateRequest;
import com.ecommerce.commondto.product.ProductResponse;
import com.ecommerce.commondto.product.ProductUpdateRequest;
import com.ecommerce.commonexception.exception.ResourceAlreadyExistsException;
import com.ecommerce.commonexception.exception.ResourceNotFoundException;
import com.ecommerce.product.model.Category;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.service.ProductManagementService;
import com.ecommerce.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductManagementServiceImpl implements ProductManagementService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @CachePut(value = "products" )
    public ProductResponse createProduct(ProductCreateRequest productCreateRequest) {
        productRepository.getProductByName(productCreateRequest.name()).ifPresent(product -> {
            throw new ResourceAlreadyExistsException(String.format("Product with name '%s' already exists", product.getName()));
        });

        Product product = new Product();
        product.setName(productCreateRequest.name());
        product.setPrice(productCreateRequest.price());
        product.setCategory(Category.from(productCreateRequest.category()));
        product.setImageUrl(productCreateRequest.imageUrl());
        product.setQuantity(productCreateRequest.quantity());
        product.setAvailable(true);

        productRepository.save(product);

        return productMapper.toProductResponse(product);
    }

    @Override
    @CacheEvict(value = "products", key = "#id")
    public ProductResponse updateProduct(Long id, ProductUpdateRequest productUpdateRequest) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Product with id %s not found", id)));

        Category category = Category.from(productUpdateRequest.category());

        existingProduct.setName(productUpdateRequest.name());
        existingProduct.setPrice(productUpdateRequest.price());
        existingProduct.setCategory(category);
        existingProduct.setImageUrl(productUpdateRequest.imageUrl());
        existingProduct.setQuantity(productUpdateRequest.quantity());

        productRepository.save(existingProduct);

        return productMapper.toProductResponse(existingProduct);
    }

    @Override
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
       productRepository.deleteById(id);
    }

}
