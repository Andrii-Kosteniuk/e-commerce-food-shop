package com.ecommerce.product.service;

import com.ecommerce.commondto.product.ProductCreateRequest;
import com.ecommerce.commondto.product.ProductResponse;
import com.ecommerce.commondto.product.ProductUpdateRequest;

public interface ProductManagementService {
    ProductResponse createProduct(ProductCreateRequest productCreateRequest);
    ProductResponse updateProduct(Long id, ProductUpdateRequest productUpdateRequest);
    void deleteProduct(Long id);
}
