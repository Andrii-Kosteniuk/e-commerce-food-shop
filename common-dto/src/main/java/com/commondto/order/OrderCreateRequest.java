package com.commondto.order;

import com.commondto.product.ItemResponse;

import java.util.List;

public record OrderCreateRequest(

        List<ItemResponse> items

) {

}
