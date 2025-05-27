package com.commondto.order;

import com.commondto.product.ItemResponse;

import java.util.List;

public record OrderRequest(

        List<Long> ids
) {
}
