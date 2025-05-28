package com.ecommerce.order.feign;

import com.ecommerce.commondto.product.ItemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@FeignClient(name = "product-service", path = "/api/v1/items")
public interface FeignItemClient {

    @GetMapping("/{id}")
    ItemResponse getItemById(@PathVariable("id") Long itemId);

    @GetMapping("/item/{name}")
    ItemResponse findItemByName(@PathVariable("name") String name);

    @GetMapping
    List<ItemResponse> getAllItems();

    @PutMapping("/update-stock/{itemId}/quantity/{quantity}")
    void reduceItemStock(@PathVariable Long itemId, @PathVariable int quantity);
}
