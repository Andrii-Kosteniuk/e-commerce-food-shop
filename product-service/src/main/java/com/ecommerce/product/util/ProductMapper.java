package com.ecommerce.product.util;

import com.ecommerce.commondto.product.ProductResponse;
import com.ecommerce.product.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toProductResponse(Product product);

}
