package com.ecommerce.auth;

import com.commondto.user.UserResponse;
import com.ecommerce.auth.security.CustomUserDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    CustomUserDetails toUserDetails(UserResponse userDetails);
}
