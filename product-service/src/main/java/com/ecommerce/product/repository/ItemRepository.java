package com.ecommerce.product.repository;

import com.ecommerce.product.model.Item;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Item findByName(@NotBlank String name);
    List<Item> findByCategory(@NotBlank Item.Category category);

}
