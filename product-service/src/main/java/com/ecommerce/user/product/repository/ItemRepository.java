package com.ecommerce.user.product.repository;

import com.ecommerce.user.product.model.Item;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByName(@NotBlank String name);
    List<Item> findByCategory(@NotBlank Item.Category category);

}
