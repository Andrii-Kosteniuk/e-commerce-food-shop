package com.ecommerce.auth.util;

import com.ecommerce.auth.dto.RegisterRequest;
import com.ecommerce.auth.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User registerRequestToUser(RegisterRequest registerRequest);

}
