package com.ecommerce.product.service;

import com.ecommerce.commondto.product.ProductResponse;
import com.ecommerce.product.model.Category;
import com.ecommerce.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductCatalogService {
    Product getProductById(Long id);
    Page<ProductResponse> getProducts(Pageable pageable);
    ProductResponse getProductByName(String name);
    List<Product> getProductsByCategoryName(Category category);
}
