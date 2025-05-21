package com.ecommerce.user.product.util;

import com.ecommerce.user.product.dto.ItemCreateRequest;
import com.ecommerce.user.product.dto.ItemResponse;
import com.ecommerce.user.product.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "id", ignore = true)
    Item itemRequestToItem(ItemCreateRequest itemCreateRequest);

    @Mapping(target = "quantityInStock", source = "quantity")
    ItemResponse toItemResponse(Item item);
}
