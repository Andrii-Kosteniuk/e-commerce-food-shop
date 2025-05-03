package com.ecommerce.product.util;

import com.ecommerce.product.dto.ItemCreateRequest;
import com.ecommerce.product.dto.ItemResponse;
import com.ecommerce.product.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "id", ignore = true)
    Item itemRequestToItem(ItemCreateRequest itemCreateRequest);

    @Mapping(target = "quantityInStock", source = "quantity")
    ItemResponse toItemResponse(Item item);
}
