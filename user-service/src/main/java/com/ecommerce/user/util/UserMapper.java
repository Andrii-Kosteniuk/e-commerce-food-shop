package com.ecommerce.user.util;

import com.commondto.auth.RegisterRequest;
import com.commondto.user.UserResponse;
import com.ecommerce.user.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface UserMapper {

    User userRequestToUser(RegisterRequest request);

    UserResponse userToUserResponse(User user);
    User userResponceToUser(UserResponse userResponse);
}
