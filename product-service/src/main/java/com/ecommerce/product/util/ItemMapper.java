package com.ecommerce.product.util;

import com.commondto.product.ItemCreateRequest;
import com.commondto.product.ItemResponse;
import com.ecommerce.product.model.Item;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item itemRequestToItem(ItemCreateRequest request);

    ItemResponse toItemResponse(Item item);

}
