package com.ecommerce.product.util;

import com.ecommerce.commondto.product.ItemCreateRequest;
import com.ecommerce.commondto.product.ItemResponse;
import com.ecommerce.product.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(source = "available", target = "available")
    ItemResponse toItemResponse(Item item);

}
