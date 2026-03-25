package com.ecommerce.product.repository;

import com.ecommerce.product.model.Category;
import com.ecommerce.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> getProductsByCategory(Category category);
    Optional<Product> getProductByName(String name);

}
