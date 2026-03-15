package com.ecommerce.product.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository  {
    void decreaseStock(Long productId, int quantity);

    void increaseStock(Long productId, int quantity);

    boolean isAvailable(Long productId, int quantity);

    int getAvailableStock(Long productId);
}
