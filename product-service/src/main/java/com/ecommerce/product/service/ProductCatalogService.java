package com.ecommerce.product.service;

import com.ecommerce.product.model.Category;
import com.ecommerce.product.model.Product;

import java.util.List;

public interface ProductCatalogService {
    Product getProductById(Long id);
    List<Product> getAllProducts();
    Product getProductByName(String name);
    List<Product> getProductsByCategoryName(Category category);
    List<Product> searchProducts(Category category);
}
