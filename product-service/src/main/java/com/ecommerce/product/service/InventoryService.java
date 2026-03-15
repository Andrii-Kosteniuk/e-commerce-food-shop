package com.ecommerce.product.service;

public interface InventoryService {
    void decreaseStock(long productId, int quantity);
    void increaseStock(long productId, int quantity);

}
