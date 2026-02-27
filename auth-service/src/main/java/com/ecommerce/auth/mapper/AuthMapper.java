package com.ecommerce.auth.mapper;

import com.ecommerce.commondto.user.UserResponse;
import com.ecommerce.auth.security.CustomUserDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    CustomUserDetails toUserDetails(UserResponse userResponse);

    UserResponse toUserResponse(CustomUserDetails customUserDetails);
}
