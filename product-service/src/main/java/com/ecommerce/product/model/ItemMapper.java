package com.ecommerce.product.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(target = "id", ignore = true)
    Item itemRequestToItem(ItemRequest itemRequest);

    @Mapping(target = "quantityInStock", source = "quantity")
    ItemResponse toItemResponse(Item item);
}
