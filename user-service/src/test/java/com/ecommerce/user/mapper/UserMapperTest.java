package com.ecommerce.user.mapper;

import com.ecommerce.user.model.Role;
import com.ecommerce.user.model.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void shouldMapUserToUserResponse() {
        // ARRANGE
        var user = new User(1L, "Andrii", "Kosteniuk", "andrii@gmail.com", "pass1234", Role.USER);
        var userResponse = userMapper.userToUserResponse(user);

        // ACT
        // ASSERT
        assertNotNull(userResponse);
        assertNotNull(user.getId());
        assertEquals(user.getId(), userResponse.id());
        assertEquals(user.getFirstName(), userResponse.firstName());
        assertEquals(user.getLastName(), userResponse.lastName());
        assertEquals(user.getEmail(), userResponse.email());
        assertEquals(user.getRole().name(), userResponse.role());
    }

    @Test
    void shouldReturnNullIfUserIsNull() {
        // ARRANGE
        var userResponse = userMapper.userToUserResponse(null);

        // ACT
        // ASSERT
        assertNull(userResponse);
    }
}
