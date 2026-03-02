package com.ecommerce.user.model;

import com.ecommerce.commondto.auth.RegisterRequest;
import com.ecommerce.commondto.user.UserRequest;
import com.ecommerce.commondto.user.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse userToUserResponse(User user);
    User requestToUser(RegisterRequest request);

}
