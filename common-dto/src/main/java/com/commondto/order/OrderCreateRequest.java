package com.commondto.order;

import com.commondto.product.OrderedItemDTO;

import java.util.List;

public record OrderCreateRequest(

        List<OrderedItemDTO> items

) {

}
