package com.ecommerce.product.repository;

import com.ecommerce.product.model.Category;
import com.ecommerce.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> getProductsByCategory(Category category);

    Optional<Product> getProductByName(String name);

    @Modifying
    @Query("""
            UPDATE Product p
            SET p.quantity = p.quantity - :qty,
                p.available = CASE WHEN (p.quantity - :qty) = 0 THEN false ELSE true END
            WHERE p.id = :id
            AND p.quantity >= :qty
            """)
    int decreaseStock(@Param("id") Long id, @Param("qty") int qty);


    @Modifying
    @Query("""
        UPDATE Product p
        SET    p.quantity  = p.quantity + :qty,
               p.available = true
        WHERE  p.id = :id
        """)
    int increaseStock(@Param("id") long id, @Param("qty") int qty);
}
