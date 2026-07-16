package com.ecommerce.user.service;

import com.ecommerce.commondto.user.UserResponse;
import com.ecommerce.user.mapper.UserMapper;
import com.ecommerce.user.model.Role;
import com.ecommerce.user.model.User;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.service.impl.UserRepositoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserRepositoryServiceTests {

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserRepositoryServiceImpl service;

    @Test
    void shouldFindUserByEmailIfPresent() {
        // ARRANGE
        User savedUser = new User(1L,"Andrii","Kosteniuk","andrii-kosteniuk@gmail.com","pasword555",Role.ADMIN);

        UserResponse userResponse = new UserResponse(1L, "Andrii", "Kosteniuk", "andrii-kosteniuk@gmail.com", "ADMIN");

        when(userRepository.findByEmail("andrii-kosteniuk@gmail.com")).thenReturn(Optional.of(savedUser));
        when(userMapper.userToUserResponse(savedUser)).thenReturn(userResponse);

        // ACT

        var userByEmail = service.getUserByEmail("andrii-kosteniuk@gmail.com");

        // ASSERT
        assertNotNull(userByEmail);
        assertEquals("andrii-kosteniuk@gmail.com", userByEmail.email());

    }

    @Test
    void shouldGetUserById() {
        // ARRANGE
        User savedUser = new User(1L,"Andrii","Kosteniuk","andrii-kosteniuk@gmail.com","pasword555",Role.ADMIN);

        UserResponse userResponse = new UserResponse(1L, "Andrii", "Kosteniuk", "andrii-kosteniuk@gmail.com", "ADMIN");

        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        when(userMapper.userToUserResponse(savedUser)).thenReturn(userResponse);

        // ACT
        var userById = service.getUserById(1L);

        // ASSERT
        assertNotNull(userById);
        assertEquals("andrii-kosteniuk@gmail.com", userById.email());

    }

}
