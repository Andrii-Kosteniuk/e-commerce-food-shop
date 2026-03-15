package com.ecommerce.product.service.impl;

import com.ecommerce.commondto.product.ProductCreateRequest;
import com.ecommerce.commondto.product.ProductResponse;
import com.ecommerce.commondto.product.ProductUpdateRequest;
import com.ecommerce.commonexception.exception.ResourceAlreadyExistsException;
import com.ecommerce.commonexception.exception.ResourceNotFoundException;
import com.ecommerce.product.model.Category;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.CatalogRepository;
import com.ecommerce.product.repository.ProductManagementRepository;
import com.ecommerce.product.service.ProductManagementService;
import com.ecommerce.product.util.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductManagementServiceImpl implements ProductManagementService {

    public final ProductManagementRepository productManagementRepository;
    public final CatalogRepository catalogRepository;
    public final ProductMapper productMapper;

    @Override
    public ProductResponse createProduct(ProductCreateRequest productCreateRequest) {
        catalogRepository.getProductByName(productCreateRequest.name()).ifPresent(product -> {
            throw new ResourceAlreadyExistsException(String.format("Product with name '%s' already exists", product.getName()));
        });

        Product product = new Product();
        product.setName(productCreateRequest.name());
        product.setPrice(productCreateRequest.price());
        product.setCategory(Category.from(productCreateRequest.category()));
        product.setImageUrl(productCreateRequest.imageUrl());
        product.setQuantity(productCreateRequest.quantity());
        product.setCreatedAt(LocalDateTime.now());
        product.setAvailable(true);

        productManagementRepository.save(product);

        return productMapper.toProductResponse(product);
    }

    @Override
    public void updateProduct(Long id, ProductUpdateRequest productUpdateRequest) {
        Product existingProduct = getProductById(id);

        Category category = Category.from(productUpdateRequest.category());

        existingProduct.setName(productUpdateRequest.name());
        existingProduct.setPrice(productUpdateRequest.price());
        existingProduct.setCategory(category);
        existingProduct.setImageUrl(productUpdateRequest.imageUrl());
        existingProduct.setQuantity(productUpdateRequest.quantity());
        existingProduct.setUpdatedAt(LocalDateTime.now());
        if (productUpdateRequest.quantity() > 1) {
            existingProduct.setAvailable(true);
        }
        productManagementRepository.save(existingProduct);

    }

    @Override
    public void deleteProduct(Long id) {
        getProductById(id);
        productManagementRepository.deleteById(id);
    }


    private Product getProductById(Long id) {
        return catalogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Product with id '%d' is not found", id)));
    }


}
