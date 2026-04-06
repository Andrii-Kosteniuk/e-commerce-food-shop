package com.ecommerce.product.mapper;

import com.ecommerce.commondto.product.ProductResponse;
import com.ecommerce.product.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toProductResponse(Product product);

}
