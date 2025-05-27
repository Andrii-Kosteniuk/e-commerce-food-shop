package com.ecommerce.auth;

import com.commondto.user.UserResponse;
import com.ecommerce.auth.security.CustomUserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

    CustomUserDetails toUserDetails(UserResponse userDetails);

    UserResponse toUserResponse(CustomUserDetails principal);
}
