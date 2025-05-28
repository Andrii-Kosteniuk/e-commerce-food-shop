package com.ecommerce.commondto.order;

import java.util.List;

public record OrderRequest(

        List<OrderItemRequest> items
) {
}
