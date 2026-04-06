package com.ecommerce.user.mapper;

import com.ecommerce.commondto.user.UserResponse;
import com.ecommerce.user.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse userToUserResponse(User user);
}
