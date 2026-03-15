package com.ecommerce.product.repository;

import com.ecommerce.product.model.Category;
import com.ecommerce.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatalogRepository extends JpaRepository<Product, Long> {
    List<Product> getProductsByCategory(Category category);

    @Query(value = "SELECT * FROM products p WHERE p.name LIKE %:keyword%", nativeQuery = true)
    List<Product> findProductByKeyword(@Param(value = "keyword") String keyword);

    Optional<Product> getProductByName(String name);
}
